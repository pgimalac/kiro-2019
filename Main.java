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
        public boolean soustraite = false;
        public int groupe = -1;

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

        public void remove(int d, int qu) {
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

        public void remove(int d, int qu) {
            dfs[d] -= qu;
        }
    }

    static int Q, F, H;
    static Sommet[] fournisseurs;
    static Sommet depot, usine;

    public static void main(String[] args) {
        String[] first = lines.next().split(" ");
        Q = Integer.parseInt(first[1]);
        F = Integer.parseInt(first[3]);
        H = Integer.parseInt(first[5]);

        fournisseurs = new Sommet[F + 2];

        String[] second = lines.next().split(" ");
        depot = new Sommet(Integer.parseInt(second[1]),
                    Double.parseDouble(second[3]),
                    Double.parseDouble(second[4]));

        String[] third = lines.next().split(" ");
        usine = new Sommet(Integer.parseInt(third[1]),
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

        //bnb();
        stupide3();
        //groupes();


        // int[] association = groupes();
        // ArrayList<Integer>[] mesGroupes = new ArrayList<>()[100];

        // for (int l=0;l<association.length;l++){
        //     if(mesGroupes[association[i]]==null){
        //         mesGroupes[association[i]] = new ArrayList<>();
        //     }
        //     mesGroupes[association[i]].add(i);
        // }

        // for (ArrayList<Integer> groupe : mesGroupes) {
        //     if(groupe!=null){
        //         boolean soustraite=false;


        //         int result = bnb(matrice_adjacence);
        //         int coutfixe = 0;
        //         for (int i =0;i<groupe.length;i++){
        //             coutfixe+=fournisseurs[groupe.get(i)].getCout();
        //         }
        //         if (result >= coutfixe){
        //             soustraite=true;
        //         }

        //     }
        // }

    }

    public static void stupide3() {
        int nb = 0;
        for (Sommet s: fournisseurs){
            if (s.isFournisseur()) {
                int cost = 0;
                for (int d = 0; d < H; d++){
                    cost += (depot.voisins[s.index] + s.voisins[usine.index]) * ((s.getQuantity(d) + Q - 1) / Q);
                }
                if (s.getCout() <= cost) {
                    s.soustraite = true;
                    nb++;
                }
            }
        }

        int[] groupes = groupes();
        int nbgroupes = 0;
        for (int i = 0; i < F; i++) {
            if (!fournisseurs[i].soustraite) {
                fournisseurs[i].groupe = groupes[i];
                nbgroupes = Math.max(nbgroupes, groupes[i]);
            }
        }
        nbgroupes++;
        ArrayList<LinkedList<Sommet>> grps = new ArrayList<LinkedList<Sommet>>(nbgroupes);
        for(int i = 0; i < nbgroupes; i++) {
            grps.add(new LinkedList<Sommet>());
        }
        for (Sommet s: fournisseurs) {
            if (s.isFournisseur() && !s.soustraite) {
                grps.get(s.groupe).add(s);
            }
        }

        // int ind = 0;
        // for (int i = 0; i < F; i++){
        //     if (!fournisseurs[i].soustraite && fournisseurs[i].groupe != -1) {
        //         fournisseurs[i].groupe = ind;
        //         Sommet[] closest = new Sommet[3];
        //         for (int j = i + 1; j < F; j++) {
        //             if (!fournisseurs[j].soustraite && fournisseurs[j].groupe != -1){
        //                 int dist = fournisseurs[i].voisins[j];
        //                 if (closest[0] == null) {
        //                     closest[0] = fournisseurs[j];
        //                 } else if (closest)
        //             }
        //         }
        //     }
        // }

        System.out.print("x " + nb + " f");
        for (Sommet s: fournisseurs) {
            if (s.soustraite) {
                System.out.print(" " + s.index);
            }
        }
        System.out.println();

        int tournees = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur() && !f.soustraite) {
                for (int d = 0; d < H; d++) {
                    tournees += (f.getQuantity(d) + Q - 1) / Q;
                }
            }
        }
        System.out.println("y " + tournees);

        System.out.println("z " + nbgroupes);
        for (int i = 0; i < nbgroupes; i++) {
                System.out.print("C " + i + " n " + grps.get(i).size() + " f");
                for (Sommet s: grps.get(i)) {
                    System.out.print(" " + s.index);
                }
                System.out.println();
        }

        int id = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur() && !f.soustraite) {
                for (int d = 0; d < H; d++) {
                    nb = f.getQuantity(d);
                    while (nb > 0) {
                        if (nb >= Q) {
                            System.out.println("P " + id + " g " + f.groupe + " s " + d + " n 1 f " + f.index + " " + Math.min(nb, Q));
                        } else {
                            for (int j = f.index + 1; j < F; j++) {
                                if (fournisseurs[j].groupe == f.groupe && fournisseurs[j].getQuantity(d) > 0) {
                                    int qu = Math.min(Q - nb, fournisseurs[j].getQuantity(d));
                                    System.out.println("P " + id + " g " + f.groupe + " s " + d + " n 2 f " + f.index + " " + nb + " f " + j + " " + qu);
                                    fournisseurs[j].remove(d, qu);
                                    break;
                                }
                            }
                        }
                        nb -= Q;
                        id ++;
                    }
                }
            }
        }
    }

    public static void stupide2() {
        int nb = 0;
        int ind = 0;
        for (Sommet s: fournisseurs){
            if (s.isFournisseur()) {
                int cost = 0;
                for (int d = 0; d < H; d++){
                    cost += (depot.voisins[s.index] + s.voisins[usine.index]) * ((s.getQuantity(d) + Q - 1) / Q);
                }
                if (s.getCout() <= cost) {
                    s.soustraite = true;
                    nb++;
                } else {
                    s.groupe = ind;
                    ind ++;
                }
            }
        }

        System.out.print("x " + nb + " f");
        for (Sommet s: fournisseurs) {
            if (s.soustraite) {
                System.out.print(" " + s.index);
            }
        }
        System.out.println();

        int tournees = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur() && !f.soustraite) {
                for (int d = 0; d < H; d++) {
                    tournees += (f.getQuantity(d) + Q - 1) / Q;
                }
            }
        }
        System.out.println("y " + tournees);

        System.out.println("z " + ind);
        for (int i = 0; i < F; i++){
            if (!fournisseurs[i].soustraite){
                System.out.println("C " + fournisseurs[i].groupe + " n " + 1 + " f " + i);
            }
        }

        int id = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur() && !f.soustraite) {
                for (int d = 0; d < H; d++) {
                    nb = f.getQuantity(d);
                    while (nb > 0) {
                        System.out.println("P " + id + " g " + f.groupe + " s " + d + " n 1 f " + f.index + " " + Math.min(nb, Q));
                       nb -= Q;
                       id ++;
                    }
                }
            }
        }
    }

    // pas de groupes, pas de sous traitance, un max de tournées
    public static void stupide() {
        System.out.println("x " + 0 + " f");

        int tournees = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur()) {
                for (int d = 0; d < H; d++) {
                    tournees += (f.getQuantity(d) + Q - 1) / Q;
                }
            }
        }
        System.out.println("y " + tournees);

        System.out.println("z " + F);
        for (int i = 0; i < F; i++){
            System.out.println("C " + i + " n " + 1 + " f " + i);
        }

        int id = 0;
        for (Sommet f: fournisseurs) {
            if (f.isFournisseur()) {
                for (int d = 0; d < H; d++) {
                    int nb = f.getQuantity(d);
                    while (nb > 0) {
                        System.out.println("P " + id + " g " + f.index + " s " + d + " n 1 f " + f.index + " " + Math.min(nb, Q));
                       nb -= Q;
                       id ++;
                    }
                }
            }
        }
    }



    /*
        SYLVAIN
    */

    static int final_res = 999999999;
    static int INT_MAX   = 999999999;
    static int N = 4;
    static int[] final_path = new int[N+1];
    static boolean[] visited = new boolean[N+1];


    //Branch and bound algorithm
    public static void bnb() {
        for(int i = 0; i<final_path.length; i++){
            final_path[i] = -1;
        }
        //final_path=[-1 for i in range(N+1)]
        visited[0]=true;
        for(int i=0;i<visited.length;i++){
            visited[i]=false;
        }

        TSP();

        int somme = 0;
        for (int i=0;i<final_path.length-1;i++){
            somme+= Main.fournisseurs[final_path[i]].voisins[final_path[i+1]];
        }
    }

    public static void copyToFinal(int[] curr_path) {
        for(int i=0;i<N;i++){
            final_path[i] = curr_path[i] ;

        }
        final_path[N] = curr_path[0];
    }




    public static int firstMin(int i) {
        int mini = INT_MAX ;
        for(int k=0;k<N;k++){if (Main.fournisseurs[i].voisins[k]<mini && i != k) {
                mini = Main.fournisseurs[i].voisins[k] ;
            }
        }
        return mini ;
    }


    public static int secondMin(int i) {

        int first = INT_MAX;
        int second = INT_MAX;
        for(int j=0;j<N;j++) {
            if (i == j) {
                continue ;
            }

            if (Main.fournisseurs[i].voisins[j] <= first) {
                second = first ;
                first = Main.fournisseurs[i].voisins[j] ;
            }
            else if ((Main.fournisseurs[i].voisins[j] <= second) &&  (Main.fournisseurs[i].voisins[j] != first)) {
                second = Main.fournisseurs[i].voisins[j];
            }
        }
        return second;
    }

    public static void TSPRec(int curr_bound, int curr_weight, int level, int[] curr_path) {

        if (level==N)  {

            if (Main.fournisseurs[curr_path[level-1]].voisins[curr_path[0]] != 0)  {
                int curr_res = curr_weight + Main.fournisseurs[curr_path[level-1]].voisins[curr_path[0]];
                if (curr_res < final_res) {
                    copyToFinal(curr_path);
                    final_res = curr_res;
                }
            }
        }

        for (int i=0;i<N;i++) {
            if (Main.fournisseurs[curr_path[level-1]].voisins[i] != 0 && !visited[i]) {
                int temp = curr_bound;
                curr_weight += Main.fournisseurs[curr_path[level-1]].voisins[i];

                if (level==1) {
                  curr_bound -= ((firstMin(curr_path[level-1]) + firstMin(i))/2);
                }
                else {
                  curr_bound -= ((secondMin(curr_path[level-1]) + firstMin(i))/2);
                }

                if (curr_bound + curr_weight < final_res)  {
                    curr_path[level] = i;
                    visited[i] = true;

                    TSPRec(curr_bound, curr_weight, level+1,curr_path);
                }

                curr_weight -= Main.fournisseurs[curr_path[level-1]].voisins[i];
                curr_bound = temp;

                for (int j=0;j<N+1;j++){
                    visited[j]=false;
                }

                for(int j=0;j<level;j++){
                    visited[curr_path[j]] = true;
                }
            }
        }
    }


    public static void TSP()  {
        int curr_bound= 0;
        int[] curr_path = new int[N+1];
        for(int i=0;i<curr_path.length;i++){
            curr_path[i] = -1;
        }

        int[] visited = new int[N+1];
        for(int i=0;i<visited.length;i++){
            visited[i]=0;
        }

        for(int i=0;i<N;i++){
            curr_bound += (firstMin(i) + secondMin(i));
        }

        curr_bound = curr_bound/2 + 1; //arrondir correctement

        Main.visited[0] = true ;
        curr_path[0] = 0;

        TSPRec(curr_bound, 0, 1, curr_path);
    }


    /**
        PIERRE
    **/

    public static int[] groupes() {
    	int[][] arretes = new int[F*F][3]; // (poids,index,index)
    	int i = 0;
        System.out.println(F);
    	for (int k = 0; k < F; k++) {
    		for (int j=0;j< F;j++) {
    			if (!fournisseurs[k].soustraite && !fournisseurs[j].soustraite && k != j) {
    				arretes[i][0] = fournisseurs[k].voisins[j];
    				arretes[i][1] = k;
    				arretes[i][2] = j;
    				i++;
    			}
            }
        }
        Arrays.sort(arretes, Comparator.comparingInt(arr -> arr[0]));
    	int[] connexe = new int[F]; //destiné a contenir les groupes
        HashSet<Integer> seen = new HashSet<Integer>();
    	for (int k=0;k<F;k++) {
            if (!fournisseurs[k].soustraite) {
                seen.add(k);
            }
    		connexe[k] = k;
    	}
        int f1;
        int f2;
        i=0;

    	while(!seen.isEmpty()) { //10 CHOISI ARBITRAIREMENT            4!!!!!!!!!
    		f1 = arretes[i][1];
    		f2 = arretes[i][2];
    		int compteur1 =0;
    		int compteur2 = 0;
    		for (int j=0;j<F;j++) {
                if (!fournisseurs[j].soustraite)
        			if (connexe[j]==connexe[f1])compteur1++;
    		}
    		for (int j=0;j<F;j++) {
                if (!fournisseurs[j].soustraite)
        			if (connexe[j]==connexe[f2])compteur2++;
    		}
    		if (compteur1+compteur2<4) {
    			int t = connexe[f2];
    			for (int j=0;j<F;j++) {
    				if (!fournisseurs[j].soustraite && connexe[j]==t) {
                        connexe[j]=connexe[f1];
                        seen.remove(f1);
                        seen.remove(f2);
                    }
                }
            }

            i++;
        }

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        int ind = 0;

        for (i = 0; i < F; i++) {
            if (fournisseurs[i].soustraite)
                continue;
            if (!map.containsKey(connexe[i])) {
                map.put(connexe[i], ind);
                ind++;
            }
            connexe[i] = map.get(connexe[i]);
        }

      return connexe;
  }
}
