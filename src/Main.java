public class Main{
    public static void main(String[] args) {
        NybCProgram nybCProgram = new NybCProgram();

        if(args.length == 0) {
            nybCProgram.StartProgram();
            return;
        }
        nybCProgram.StartProgram(args[0]);
    }
}
