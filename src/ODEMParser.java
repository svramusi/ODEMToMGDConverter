public class ODEMParser {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Enter the name of the ODEM file to parse and output MDG file!");
            return;
        }

        String odemFile = args[0];
        String mdgFile = args[1];

        Parser parser = new Parser(odemFile, mdgFile);
        parser.parse();
        parser.createMDGFile();
    }
}
