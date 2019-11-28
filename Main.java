import java.io.*;
import java.util.*;

public class Main {

    static Iterator<String> lines =
        (new BufferedReader(new InputStreamReader(System.in))).lines().iterator();

    static class Sommet {
        public final int index;
        public final double gps1;
        public final double gps2;
        public final int[] voisins;

        public Sommet(int index, double gps1, double gps2) {
            this.index = index;
            this.gps1 = gps1;
            this.gps2 = gps2;
            Main.fournisseurs[index] = this;
            voisins = new int[Main.F + 2];
        }

        public boolean isFournisseur() {
            return false;
        }

        public int getCout() {
            throw new RuntimeException();
        }

        public int getQuantity(int day) {
            throw new RuntimeException();
        }
    }

    static class Fournisseur extends Sommet {
        public final int cout;
        public final int[] dfs;

        public Fournisseur(String[] line) {
            super(Integer.parseInt(line[1]),
                Double.parseDouble(line[line.length - 2]),
                Double.parseDouble(line[line.length - 2]));
            cout  = Integer.parseInt(line[3]);
            dfs = new int[Main.H];
            for (int i = 0; i < Main.H; i ++) {
                dfs[i] = Integer.parseInt(line[i + 5]);
            }
        }

        public boolean isFournisseur() {
            return true;
        }

        public int getCout() {
            return cout;
        }

        public int getQuantity(int day) {
            return dfs[day];
        }
    }

    static int Q, F, H;
    static Sommet[] fournisseurs;

    public static void main(String[] args) {
        String[] first = lines.next().split(" ");
        Q = Integer.parseInt(first[1]);
        F = Integer.parseInt(first[3]);
        H = Integer.parseInt(first[5]);

        fournisseurs = new Sommet[F + 2];

        String[] second = lines.next().split(" ");
        new Sommet(Integer.parseInt(second[1]),
                    Double.parseDouble(second[3]),
                    Double.parseDouble(second[4]));

        String[] third = lines.next().split(" ");
        new Sommet(Integer.parseInt(third[1]),
                    Double.parseDouble(third[3]),
                    Double.parseDouble(third[4]));

        for (int i = 0; i < F; i++) {
            new Fournisseur(lines.next().split(" "));
        }

        while (lines.hasNext()) {
            String[] line = lines.next().split(" ");
            fournisseurs[Integer.parseInt(line[1])]
                .voisins[Integer.parseInt(line[2])] = Integer.parseInt(line[4]);
        }
        
        stupide();
        groupes();
    }

    public static void stupide() {
        System.out.println(0 + " f");
        System.out.println("???");
        System.out.println("z " + F);
        for (int i = 0; i < F; i++){
            System.out.println("C " + i + " n " + 1 + " f " + i);
        }
        System.out.println("");
    }
    
    public static void groupes() {
    	Sommet[][] arretes = new Sommet[3][1763];
    	int i = 0;
    	for (Sommet f:fournisseurs) {
    		for (int j=0;j<(f.voisins).length;j++) {
    			
    		}
    		}
    }
}
