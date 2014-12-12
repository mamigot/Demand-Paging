Demand Paging
=================
**Miguel Amigot**
<br>
*Operating Systems, Fall 2014*


Simulation of RANDOM, FIFO and LRU demand paging algorithms under different conditions, which are specified when executing the program according to the following format. Helper classes such as Process.java, Frame.java and JobMixProbability.java are used to run the main program, DemandPaging.java.

Its main method accepts six command-line arguments detailing the parameters and initial conditions for the simulation (five integers and one string).They are:
* M, the machine size in words.
* P, the page size in words.
* S, the size of a process, i.e., the references are to virtual addresses 0...S-1.
* J, the "job mix", which determines the number of simulated processes as well as parameters pertaining to locality.
* N, the number of references for each process.
* R, the replacement algorithm, FIFO, RANDOM, or LRU.


### Compile
In the directory with DemandPaging.java, Process.java, Frame.java and JobMixProbability.java, type:

```
javac *.java
```

### Execute
Provide six arguments according to the listed convention when executing the main program, DemandPaging.java. For example,

```
java DemandPaging 20 10 10 3 10 lru
```
