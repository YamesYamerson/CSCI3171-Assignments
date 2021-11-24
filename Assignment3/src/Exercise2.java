//Thread Example 3
//Creates three threads, runs them concurrently but each thread 
//is paused for a random time between 0 and 3 seconds

public class Exercise2 extends Thread{
    public static void main(String[] args){
        Exercise2 thread0 = new Exercise2(), thread1 = new Exercise2(), thread2 = new Exercise2(),
                thread3 = new Exercise2(), thread4 = new Exercise2();
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
        boolean catchLightSaber = false;
        for (int i=0; i<5; i++)
        {
            try{
                pause = (int)(Math.random()*3000);
                if(!catchLightSaber){
                    System.out.println(getName() + " throws the lightsaber!");
                    sleep(pause);
                    catchLightSaber = true;
                }else{
                    System.out.println("                                    " + getName() + " catches the lightsaber!");
                    sleep(pause);
                    catchLightSaber = false;
                }

            }
            catch (InterruptedException e){
                System.out.println(e);
            }
        }
    }
}