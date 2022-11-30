

import java.time.Instant;

public class TestRunner
{
    public static Writer logWriter;
    GeneticAlgorithm ga;
    int MAX_RUN;
    int MAX_LENGTH;
    long[] runtimes;

    /* Instantiates the TestRunner class
     *
     */
    public TestRunner()
    {
        logWriter = new Writer();
        MAX_RUN = 1;
        runtimes = new long[MAX_RUN];
    }

    /* Test method accepts the N/max length, and parameters mutation rate and max epoch to set for the GA accordingly.
     *
     * @param: max length/n
     * @param: mutation rate for GA
     * @param: max epoch for GA
     */
    public void test(int maxLength, double mutationRate, int maxEpoch)
    {
        MAX_LENGTH = maxLength;
        ga = new GeneticAlgorithm(MAX_LENGTH);                                        //define ga here
        ga.setMutation(mutationRate);
        ga.setEpoch(maxEpoch);
        long testStart = System.nanoTime();
        String filepath = "Задача-с-кралици-генетичен-алгоритъм-фн72085-" + MAX_LENGTH + "-" + mutationRate + "-" + maxEpoch + ".txt";
        Instant startTime = Instant.now();
        Instant endTime = Instant.now();
        Instant totalTime = Instant.now();
        int fail = 0;
        int success = 0;

        logParameters();

        for (int i = 0; i < MAX_RUN; )
        {                                                //run 50 success to pass criteria
            startTime = Instant.now();
            if (ga.algorithm())
            {
                endTime = Instant.now();
                totalTime = endTime.minusNanos(startTime.getNano());
                System.out.println("Done");
                System.out.println("run " + (i + 1));
                System.out.println("time in nanoseconds: " + totalTime.getNano());
                System.out.println("Success!");

                runtimes[i] = totalTime.getNano();
                i++;
                success++;

                //write to log
                logWriter.add(("Run: " + i));
                logWriter.add(("Runtime in nanoseconds: " + totalTime.getNano()));
                logWriter.add(("Found at epoch: " + ga.getEpoch()));
                logWriter.add(("Population size: " + ga.getPopSize()));
                logWriter.add("");


                for (Chromosome c : ga.getSolutions())
                {
                    logWriter.add("-----SOLUTION-----TEST RUN " + i);//write solutions to log file
                    logWriter.add(c);
                    logWriter.add("-----SOLUTION-----TEST RUN " + i);
                    logWriter.add("");
                }
            } else
            {                                                                //count failures for failing criteria
                fail++;
                System.out.println("Fail!");
            }

            if (fail >= 100)
            {
                System.out.println("Cannot find solution with these params");
                break;
            }
            startTime = Instant.now();                                                            //reset time
            endTime = Instant.now();
            totalTime = Instant.now();
        }

        System.out.println("Number of Success: " + success);
        System.out.println("Number of failures: " + fail);

        long testEnd = System.nanoTime();
        logWriter.add("start: " + startTime);
        logWriter.add("end: " + endTime);
        logWriter.add("elapsed nanoseconds: " + (testEnd - testStart));


        logWriter.writeFile(filepath);
        printRuntimes();
    }

    /* Converts the parameters of GA to string and adds it to the string list in the writer class
     *
     */
    public void logParameters()
    {
        logWriter.add("Genetic Algorithm");
        logWriter.add("Parameters");
        logWriter.add("MAX_LENGTH/N: " + MAX_LENGTH);
        logWriter.add("STARTING_POPULATION: " + ga.getStartSize());
        logWriter.add("MAX_EPOCHS: " + ga.getMaxEpoch());
        logWriter.add("MATING_PROBABILITY: " + ga.getMatingProb());
        logWriter.add("MUTATION_RATE: " + ga.getMutationRate());
        logWriter.add("MIN_SELECTED_PARENTS: " + ga.getMinSelect());
        logWriter.add("MAX_SELECTED_PARENTS: " + ga.getMaxSelect());
        logWriter.add("OFFSPRING_PER_GENERATION: " + ga.getOffspring());
        logWriter.add("MINIMUM_SHUFFLES: " + ga.getShuffleMin());
        logWriter.add("MAXIMUM_SHUFFLES: " + ga.getShuffleMax());
        logWriter.add("");
    }

    /* Prints the runtime summary in the console
     *
     */
    public void printRuntimes()
    {
        for (long x : runtimes)
        {
            System.out.println("run with time " + x + " nanoseconds");
        }
    }

    public static void main(String[] args)
    {
        TestRunner tester = new TestRunner();

        tester.test(8, 0.001, 1000);

    }
}
