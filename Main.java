import java.io.*;
import java.util.*;

public class Main {

    static Iterator<String> lines =
        (new BufferedReader(new InputStreamReader(System.in))).lines().iterator();

    static class Groupe {
        ArrayList<Fournisseur> sesFournisseurs = new ArrayList<>();
        public boolean is_soustraite=false;
        public boolean best_path = new int[0];
        public Groupe(){

        }

    }

     static class Couplee (){
        int[] final_path= new int[0];
        int sommme=0;
        public class Couplee(int[] final_path, int somme){
            this.final_path=final_path;
            this.somme=somme;

        }
    }

    static class Couple (){
        Fournisseur fournisseur;
        int quantiteprise;

        public Couple(Fournisseur fournisseur, int quantiteprise){
            this.fournisseur=fournisseur;
            this.quantiteprise=quantiteprise;
        }
    }

    

    static class Sommet {
        public final int index;
        public final double gps1;
        public final double gps2;
        public final int[] voisins;
        public boolean soustraite=false;
        public int groupe = 0;
        //Tournée : semaine, identifiant, liste (fournisseur quantité)

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
        int nombre_de_groupe = 0;

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

        //bnb();
        stupide();
        //groupes();


        int[] association = groupes();
        Groupe[] mesGroupes = new Groupe[100];

        for (int l=0;l<association.length;l++){
            if(mesGroupes[association[i]]==null){
                mesGroupes[association[i]] = new Groupe();
            }
            mesGroupes[association[i]].sesFournisseurs.add(i);
        }

        for (Groupe groupe : mesGroupes) {
            if(groupe!=null){
                boolean soustraite=false;
                nombre_de_groupe+=1;

                int[][] matrice_adjacence = new int[groupe.sesSuccesseurs.length][groupe.sesSuccesseurs.length];

                for(int k=0; k<groupe.sesSuccesseurs.length;k++){
                    for(int u=0; u<groupe.sesSuccesseurs.length;u++){
                        matrice_adjacence[u][k]=fournisseurs[u].voisins[k].getCout();
                    }
                }


                Couplee data = bnb(matrice_adjacence); //TODO : Remettre les int[][]
                int result = data.somme;
                int[] best_path = data.final_path; //TODO : Faire renvoyer le best path à bnb
                groupe.best_path=best_path;
                int coutfixe = 0;
                for (int i =0;i<groupe.length;i++){
                    coutfixe+=fournisseurs[groupe.get(i)].getCout();
                }
                if (result >= coutfixe){
                    soustraite=true;
                }
                groupe.is_soustraite=soustraite;
                for(int f : groupe.sesSuccesseurs ){
                    fournisseurs[f].soustraite=soustraite;
                }


            }
        }
        String gens_sous_traite = ""
        int compteur = 0 ;
        for(Fournisseur f : fournisseurs){
            if(f.soustraite){
                gens_sous_traite += f.index + " "
                compteur++;
            }
        
        }

        ArrayList<Fournisseur>() nonSousTraites = new ArrayList<>();

        for(Fournisseur element : fournisseurs){
            if (!element.soustraite) {
                nonSousTraites.add(element);
            }
        }

        ArrayList<Groupe>() groupe_non_sous_traite = new ArrayList<>();

        for (Groupe groupe : mesGroupes) {
            if(!groupe.is_soustraite){
               groupe_non_sous_traite.add(groupe);
            }
        }



        
        int remplir = 0;
        int avant = 0;
        int apres = 0;

        ArrayList<ArrayList<Couple>>[] trajet_par_semaine = new ArrayList<ArrayList<Couple>>[H];

        ArrayList<ArrayList<Couple>> allTrajets = new ArrayList<>();

        for (int s=0;s<trajet_par_semaine.length;s++){
            allTrajets = new ArrayList<>();

            for(Groupe groupe : groupe_non_sous_traite){
                for(Fournisseur fournisseur : groupe.sesSuccesseurs){
                    arecuperer+=fournisseur.getQuantity(s);
                }
                int curseur = 0;
                current_fournisseur=groupe.sesSuccesseurs.get(curseur);
                while (curseur<groupe.sesSuccesseurs.length){
                    remplir=0;
                    while (remplir<Q || curseur>=groupe.sesSuccesseurs.length){
                        ArrayList<Couple> currentTrajet = new ArrayList<>();
                        avant= current_fournisseur.getQuantity(s);
                        while(current_fournisseur.getQuantity(s)>0){
                            current_fournisseur.getQuantity(s)-=1;
                            remplir+=1;
                        }
                        apres = current_fournisseur.getQuantity(s);
                        if (apres-avant>0){
                            currentTrajet.add(Couple(current_fournisseur,apres-avant))
                        }
                        curseur+=1;
                        current_fournisseur=groupe.sesSuccesseurs.get(curseur);

                    }
                    allTrajets.add(currentTrajet);
                   
                }


            }
            trajet_par_semaine[s]=allTrajets;
        }

        System.out.println("x " + compteur + "f " + gens_sous_traite);
        System.out.println("y " + nombre_de_tournees);
        System.out.println("z " +  nombre_de_groupe);

        String mesFournisseurs= "";

        int nombre_de_tournees= 0;

        for(int s= 0; s<H;s++){
            nombre_de_tournees+=trajet_par_semaine[s].length;
        }
        for(int s= 0; s<H;s++){ //chaque semaine
            for(int k=0; k<trajet_par_semaine[s].length;k++){ // dans chaque all trajet
                for (int t=0; t<trajet_par_semaine[s].get(k).length;t++){ // dans chaque trajet
                    mesFournisseurs="";
                    for(int e=0; e<trajet_par_semaine[s].get(k).get(t).length;e++){ //dans chaque étape
                        mesFournisseurs+="f " +trajet_par_semaine[s].get(k).get(t).get(e).fournisseur.index + " " + trajet_par_semaine[s].get(k).get(t).get(e).quantiteprise +" ";
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

        System.out.println("");
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
    public static Couplee bnb(int[][] mat) {
        for(int i = 0; i<final_path.length; i++){
            final_path[i] = -1;
        }
        //final_path=[-1 for i in range(N+1)]
        visited[0]=true;
        for(int i=0;i<visited.length;i++){
            visited[i]=false;
        }

        TSP(mat);

        int somme = 0;
        for (int i=0;i<final_path.length-1;i++){
            somme+= math[final_path[i]][final_path[i+1]];
        }
        return new Couplee(final_path,somme);
    }

    public static void copyToFinal(int[] curr_path) {
        for(int i=0;i<N;i++){
            final_path[i] = curr_path[i] ;

        }
        final_path[N] = curr_path[0];
    }




    public static int firstMin(int i) {
        int mini = INT_MAX ;
        for(int k=0;k<N;k++){if (mat[i][k]<mini && i != k) {
                mini = mat[i][k] ;
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

            if (mat[i][j] <= first) {
                second = first ;
                first = mat[i][j] ;
            }
            else if ((mat[i][j] <= second) &&  (mat[i][j] != first)) {
                second = mat[i][j];
            }
        }
        return second;
    }

    public static void TSPRec(int[][] mat,int curr_bound, int curr_weight, int level, int[] curr_path) {

        if (level==N)  {

            if (mat[curr_path[level-1]][curr_path[0]] != 0)  {
                int curr_res = curr_weight + mat[curr_path[level-1]][curr_path[0]];
                if (curr_res < final_res) {
                    copyToFinal(curr_path);
                    final_res = curr_res;
                }
            }
        }

        for (int i=0;i<N;i++) {
            if (mat[curr_path[level-1]][i] != 0 && !visited[i]) {
                int temp = curr_bound;
                curr_weight += mat[curr_path[level-1]][i];

                if (level==1) {
                  curr_bound -= ((firstMin(curr_path[level-1]) + firstMin(i))/2);
                }
                else {
                  curr_bound -= ((secondMin(curr_path[level-1]) + firstMin(i))/2);
                }

                if (curr_bound + curr_weight < final_res)  {
                    curr_path[level] = i;
                    visited[i] = true;

                    TSPRec(mat,curr_bound, curr_weight, level+1,curr_path);
                }

                curr_weight -= mat[curr_path[level-1]][i];
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


    public static void TSP(int[][] mat)  {
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

        TSPRec(mat,curr_bound, 0, 1, curr_path);
    }


    /**
        PIERRE
    **/

      public static int[] groupes() {
    	int[][] arretes = new int[3][1763]; // (poids,index,index)
    	int i = 0;
    	for (Sommet f:fournisseurs) {
    		for (int j=0;j<(f.voisins).length;j++) {
    			if (i!=j) {
    				arretes[i][0] = f.voisins[j];
    				arretes[i][1] = f.index;
    				arretes[i][j] = j;
    				i++;
    			}
    			}
    		}
    	Arrays.sort(arretes, Comparator.comparingInt(arr -> arr[0]));
    	int[] connexe = new int[F+2]; //destiné a contenir les groupes
    	for (int k=0;k<F;k++) {
    		connexe[k] = k;
    	}
    	boolean test = true;

		int f1;
		int f2;
		i=0;
		int count = 0;
    	while(count<4) { //10 CHOISI ARBITRAIREMENT            4!!!!!!!!!
    		f1 = arretes[i][1];
    		f2 = arretes[i][2];
    		int t = connexe[f2];
    		for (int j=0;j<F;j++) {
    			if (connexe[j]==t)connexe[j]=connexe[f1];
    		}
    		for (int j=0;j<F;j++) {
    			if (connexe[j]==connexe[f1])count++;
    		}
    		i++;
    		}
    	
        return connexe;
    }
}
