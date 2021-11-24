//Thread Example 3 
//Creates three threads, runs them concurrently but each thread 
//is paused for a random time between 0 and 3 seconds 

public class Exercise1 extends Thread{
    public static void main(String[] args){
        Exercise1 thread0 = new Exercise1(), thread1 = new Exercise1(), thread2 = new Exercise1(),
                thread3 = new Exercise1(), thread4 = new Exercise1();
        String[] names = {"Han Solo", "Darth Vader", "Luke Skywalker", "Chewbacca", "BB-8"};
        Thread[] threads = {thread0, thread1,thread2, thread3, thread4};

        for(int i = 0; i<5; i++){
            threads[i].setName(names[i]);
        }
        for(int i = 0; i<5; i++){
            threads[i].start();
        }
    }
    public void run(){
        int pause;
        for (int i=0; i<5; i++){
            try{
                System.out.println(getName() + " awake!");
                        pause = (int)(Math.random()*3000);
                System.out.println("Sleep time for "+
                        getName() + ": " +pause + " millisecs");
                sleep(pause);
            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }
    }
}