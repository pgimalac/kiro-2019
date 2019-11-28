//Branch and bound algorithm

int N = 4;
int[] final_path = new int[N+1];
for(int i=0; i<final_path.length; i++){
    final_path[i] = -1; 
}
//final_path=[-1 for i in range(N+1)]
boolean[] visited = new boolean[N+1];
visited[0]=true;
for(int i=0;i<visited.length;i++){
    visited[i]=false;
}

int final_res = 999999999;
int INT_MAX   = 999999999;
  

public void copyToFinal(curr_path) {
    for(int i=0;i<N;i++){
        final_path[i] = curr_path[i] ;
    
    }
    final_path[N] = curr_path[0];
}




public int firstMin(adj, i) {
    int mini = INT_MAX ;
    for(int k=0;k<N;k++){if (adj[i][k]<mini and i != k) {
            mini = adj[i][k] ;
        }
    }
    return mini ;
}


public int secondMin(adj,  i) {

    int first = INT_MAX;
    int second = INT_MAX;
    for(int j=0;j<N;j++) {
        if (i == j) {
            continue ;
        }
  
        if (adj[i][j] <= first) {
            second = first ;
            first = adj[i][j] ;
        }
        else if ((adj[i][j] <= second) &&  (adj[i][j] != first)) {
            second = adj[i][j];
        }
    }
    return second;
}
  

public void TSPRec( adj, curr_bound, curr_weight, level, curr_path) {
    
    if (level==N)  {
   
        if (adj[curr_path[level-1]][curr_path[0]] != 0)  {
            curr_res = curr_weight + adj[curr_path[level-1]][curr_path[0]];
            if (curr_res < final_res) {
                copyToFinal(curr_path);
                final_res = curr_res;
            }    
        }
    }

  
   
    for (int i=0;i<N;i++) {
        if (adj[curr_path[level-1]][i] != 0) && (!visited[i]) { 
            int temp = curr_bound;
            curr_weight += adj[curr_path[level-1]][i];
  
            if (level==1) {
              curr_bound -= ((firstMin(adj, curr_path[level-1]) + firstMin(adj, i))/2);
            }
            else {
              curr_bound -= ((secondMin(adj, curr_path[level-1]) + firstMin(adj, i))/2);
            }
  
            if (curr_bound + curr_weight < final_res)  {
                curr_path[level] = i;
                visited[i] = True;
  
                TSPRec(adj, curr_bound, curr_weight, level+1,curr_path);
            }
  
            curr_weight -= adj[curr_path[level-1]][i];
            curr_bound = temp; 
            
            for (int i=0;i<N+1;i++){
                visited[i]=false;
            }
            
            for(int i=0;i<level;i++){
                visited[curr_path[i]] = true;

            }
        }
    }
}            
   

public void TSP(adj)  {
    int curr_bound= 0;
    int[] curr_path= new int[N+1];
    for(int i=0;i<curr_path.length;i++){
        curr_path[i]=-1;
    }
  
    int[] visited = new int[N+1];
    for(int i=0;i<visited.length;i++){
        visited[i]=0;
    }
  
    for(int i=0;i<N;i++){
        curr_bound += (firstMin(adj, i) + secondMin(adj, i));
    }
  
    curr_bound = curr_bound/2 + 1; //arrondir correctement
  
    visited[0] = True ;
    curr_path[0] = 0;

    TSPRec(adj, curr_bound, 0, 1, curr_path);
}

  
adj = [[0, 10, 15, 20],[10, 0, 35, 25],[15, 35, 0, 30], [20, 25, 30, 0] ] 
