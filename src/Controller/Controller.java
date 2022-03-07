package Controller;

import Model.Exceptions.InterpreterException;
import Model.PrgState;
import Model.adt.*;
import Model.value.IValue;
import Model.value.RefValue;
import Repo.IRepo;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


public class Controller {

    IRepo repo;
    ExecutorService executor;

    public Controller(IRepo repo) {
        this.repo = repo;
    }

    public void setLogFilePath(String logFilePath) {
        repo.setLogFilePath(logFilePath);
    }
    public void addProgram(PrgState newPrg) {
        repo.addPrg(newPrg);
    }

    public void oneStep() throws InterpreterException, ExecutionException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgStates = removeCompletedPrg(repo.getStates());
        IHeap heap = prgStates.get(0).getHeap();
        heap.setContent(garbageCollector(getAddresses(prgStates.stream().map(prgState -> prgState.getSymTable().getContent().values()).collect(Collectors.toList()), heap.getContent()), heap.getContent()));
        oneStepForAllPrg(prgStates);
        prgStates = removeCompletedPrg(repo.getStates());
        executor.shutdownNow();
        repo.setPrgList(prgStates);
    }

    public void allStep() throws ExecutionException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgStates = removeCompletedPrg(repo.getStates());
        while(!prgStates.isEmpty()){
            IHeap heap = prgStates.get(0).getHeap();
            heap.setContent(garbageCollector(getAddresses(prgStates.stream().map(prgState -> prgState.getSymTable().getContent().values()).collect(Collectors.toList()), heap.getContent()), heap.getContent()));
            oneStepForAllPrg(prgStates);
            prgStates = removeCompletedPrg(repo.getStates());

        }
        executor.shutdownNow();
        repo.setPrgList(prgStates);
    }

    Set<Integer> getAddresses(List<Collection<IValue>> symTableValues, Map<Integer, IValue> heap) {
        Set<Integer> addresses = new HashSet<>();
        symTableValues.forEach(symTable -> symTable.stream().filter(v -> v instanceof RefValue).forEach(v -> {
            while (v instanceof RefValue) {
                addresses.add(((RefValue) v).getAddress());
                v = heap.get(((RefValue) v).getAddress());
            }
        }));
        return addresses;
    }

    Map<Integer, IValue> garbageCollector(Set<Integer> addresses, Map<Integer, IValue> heap) {
        return heap.entrySet().stream().filter(e -> addresses.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<PrgState> getStates() {
        return repo.getStates();
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream().filter(PrgState::isNotCompleted).collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgStates) throws ExecutionException {
        prgStates.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (InterpreterException e) {
                System.out.println(e.getMessage());
            }
        });
        List<Callable<PrgState>> callList = prgStates.stream().map(p -> (Callable<PrgState>) (p::oneStep)).collect(Collectors.toList());
        try{
            if(executor == null){
                executor = Executors.newFixedThreadPool(2);
            }
            List<PrgState> newPrgList = new ArrayList<>();
            for (Future<PrgState> future : executor.invokeAll(callList)) {
                PrgState prgState = future.get();
                if (prgState != null) {
                    newPrgList.add(prgState);
                }
            }
            prgStates.addAll(newPrgList);
            prgStates.forEach(prg-> {
                try {
                    repo.logPrgStateExec(prg);
                } catch (InterpreterException e) {
                    System.out.println("here2");
                    System.out.println(e.getMessage());
                }
            });
            repo.setPrgList(prgStates);}
        catch (InterruptedException ignored){

        }
    }
}
