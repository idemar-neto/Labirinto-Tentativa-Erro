//NOME: Idemar Burssed dos Santos Neto - NºUSP: 11857282

import java.io.*;
import java.util.*;

class Item {

	private int lin, col, value, weight;

	public Item(String s){

		String [] parts = s.split(" +");		
		lin = Integer.parseInt(parts[0]);
		col = Integer.parseInt(parts[1]);
		value = Integer.parseInt(parts[2]);
		weight = Integer.parseInt(parts[3]);
	}

	public Item(int lin, int col, int value, int weight){

		this.lin = lin;
		this.col = col;
		this.value = value;
		this.weight = weight;//weight
	}

	public int getLin(){

		return lin;
	}

	public int getCol(){

		return col;
	}

	public int [] getCoordinates(){

		return new int [] { getLin(), getCol() } ;
	}

	public int getValue(){

		return value;
	}

	public int getWeight(){

		return weight;
	}

	public String toString(){

		return "Item: coordinates = (" + getLin() + ", " + getCol() + "), value = " + getValue() + " weight = " + getWeight();
	}
}

class Map {

	public static final char FREE = '.';

	private char [][] map;
	private Item [] items;
	private int nLin, nCol, nItems, startLin, startCol, endLin, endCol, size;

	public Map(String fileName){

		try{

			BufferedReader in = new BufferedReader(new FileReader(fileName));

			Scanner scanner = new Scanner(new File(fileName));

			
			nLin = scanner.nextInt();
			nCol = scanner.nextInt();

			map = new char[nLin][nCol];
			size = 0;

			for(int i = 0; i < nLin; i++){

				String line = scanner.next();
			
				for(int j = 0; j < nCol; j++){

					map[i][j] = line.charAt(j);

					if(free(i, j)) size++;
				}
			}

			nItems = scanner.nextInt();
			items = new Item[nItems];

			for(int i = 0; i < nItems; i++){

				items[i] = new Item(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
			}

			startLin = scanner.nextInt();
			startCol = scanner.nextInt();
			endLin = scanner.nextInt();
			endCol = scanner.nextInt();
		}
		catch(IOException e){

			System.out.println("Error loading map... :(");
			e.printStackTrace();
		}
	}

	public void print(){

		System.out.println("Map size (lines x columns): " + nLin + " x " + nCol);

		for(int i = 0; i < nLin; i++){

			for(int j = 0; j < nCol; j++){

				System.out.print(map[i][j]);
			}

			System.out.println();
		}

		System.out.println("Number of items: " + nItems);

		for(int i = 0; i < nItems; i++){

			System.out.println(items[i]);
		}
	}

	public boolean blocked(int lin, int col){

		return !free(lin, col);
	}

	public boolean free(int lin, int col){

		return map[lin][col] == FREE; 
	}

	public void step(int lin, int col){

		map[lin][col] = '*';
	}

	public void unstep(int lin, int col){

		if(map[lin][col] == '*') map[lin][col] = '.';	
	}

	public boolean finished(int lin, int col){

		return (lin == endLin && col == endCol); 
	}

	public int getStartLin(){

		return startLin;
	}

	public int getStartCol(){

		return startCol;
	}

	public int getEndLin(){

		return endLin;
	}

	public int getEndCol(){

		return endCol;
	}

	public int getSize(){

		return size;
	}

	public int nLines(){

		return nLin;
	}

	public int nColumns(){

		return nCol;
	}

	public Item getItem(int lin, int col){

		for(int i = 0; i < items.length; i++){

			Item item = items[i];

			if(item.getLin() == lin && item.getCol() == col) return item;
		}

		return null;
	}
}

class Solucao { 

	public static final int MAX = 100;

	int index;
	int [] valores;

	public Solucao(Map map){ //cria um vetor para a realização das tentaivas de achar um caminho otimo

		index = 3;
		valores = new int[MAX];
		valores[1] = map.getEndLin();
		valores[2] = map.getEndCol();
	}

	public void adiciona(int lin, int col){ //adiciona as coordenadas de um possivel caminho

		valores[index] = lin;
		valores[index+1] = col;
		index = index + 2;
		valores[0] = index;
	}

	public int tamanho() { //retorna o numero de passos dados para o objetivo

		return (valores[0]-1)/2;
	}

	public void vetor(int [] v){ //copia os valores de um caminho para um vetor especifico

		for(int i = 0; i < index; i++) v[i]=valores[i];
	}
	
	public int valor(Map map){ // calculo do valor dos itens de um possivel caminho otimo

		int path_size = valores[0];
		int totalValue = 0;

		for(int i = path_size-2; i > 2; i -= 2){

			int lin = valores[i];
			int col = valores[i + 1];
			Item item = map.getItem(lin, col);

			if(item != null){
				totalValue += item.getValue();
			}
		}

		return totalValue;
	}

	public double time(Map map){ //calculo do tempo do possivel caminho otimo por passo
		
		double weight = 0;
		double time = 0.0;

		int path_size = valores[0];

		for(int i = path_size-2; i > 2; i -= 2){

			int lin = valores[i];
			int col = valores[i + 1];
			Item item = map.getItem(lin, col);

			if(item != null){
				weight += item.getWeight();
			}
				
			time += (1+(weight/10))*(1+(weight/10));
		}

		return time;
	}

	public void print(){

		System.out.print("Solução:");

		for(int i = 0; i < index; i++){

			System.out.print(" " + valores[i]);
		}

		System.out.println();
	}
}


public class EP2 {

	public static final boolean DEBUG = false;

	public static double time(Map map, int [] path){ //Funcao retorna o tempo de dado caminho escolhido
		
		double weight = 0;
		double time = 0.0;

		int path_size = path[0];

		for(int i = path_size-2; i > 2; i -= 2){

			int lin = path[i];
			int col = path[i + 1];
			Item item = map.getItem(lin, col);

			if(item != null){
				weight += item.getWeight();
			}
				
			time += (1+(weight/10))*(1+(weight/10));
		}

		return time;
	}


	public static Solucao tentativa_e_erro(Map map, int lin, int col, int criteria){

		//VARIAVEIS:
		//	map: importa o mapa do labirinto
		//	lin: linha da coordenada
		//	col: coluna da coordenada
		//	criteria: criterio para o melhor caminho 

		//TENTATIVA E ERRO:
		//		map.step(lin, col); : marca o lugar onde está antes de avançar e ver o proximo passo
		//		Solucao solucao = tentativa_e_erro(map,lin, col, criteria) : verifica os proximos passos possíveis
		//		map.unstep(lin,col); : desmarca o passo para tentar outras alternativas de caminho


		//CRITERIOS:
		//		case 1 : Caminho mais curto (menos passos)
		//		case 2 : Caminho mais longo (mais casas)
		//		case 3 : Caminho mais valioso
		//		case 4 : Caminho mais curto (menos tempo)

		//System.out.println(lin + "  " + col);

		if(map.finished(lin,col)) return new Solucao(map);//retorna um caminho possivel

		Solucao melhor = null;

			if(lin - 1 >= 0 && map.free(lin - 1, col)){ //verifica a viabilidade de mover-se para cima

				map.step(lin, col);
				Solucao solucao = tentativa_e_erro(map, lin - 1, col, criteria);
				map.unstep(lin,col);

				switch(criteria){
					case 1 : if(solucao != null && (melhor == null || solucao.tamanho() < melhor.tamanho()))
								melhor = solucao;break;

					case 2 : if(solucao != null && (melhor == null || solucao.tamanho() >= melhor.tamanho()))
								melhor = solucao;break;

					case 3 : if(solucao != null && (melhor == null || solucao.valor(map) > melhor.valor(map)))
								melhor = solucao;;break;

					case 4 : if(solucao != null && (melhor == null || solucao.time(map) < melhor.time(map)))
								melhor = solucao;break;

				}

			}
			if(col + 1 < map.nColumns() && map.free(lin, col + 1)){ //verifica a viabilidade de mover-se para direita
				map.step(lin, col);
				Solucao solucao = tentativa_e_erro(map, lin, col + 1, criteria);
				map.unstep(lin,col);

				switch(criteria){
					case 1 : if(solucao != null && (melhor == null || solucao.tamanho() < melhor.tamanho()))
								melhor = solucao;break;

					case 2 : if(solucao != null && (melhor == null || solucao.tamanho() >= melhor.tamanho()))
								melhor = solucao;break;

					case 3 : if(solucao != null && (melhor == null || solucao.valor(map) > melhor.valor(map)))
								melhor = solucao;;break;

					case 4 : if(solucao != null && (melhor == null || solucao.time(map) < melhor.time(map)))
								melhor = solucao;break;
								
				}

			}
			if(lin + 1 < map.nLines() && map.free(lin + 1, col)){//verifica a viabilidade de mover-se para baixo

				map.step(lin, col);
				Solucao solucao = tentativa_e_erro(map, lin + 1, col, criteria);
				map.unstep(lin,col);

				switch(criteria){
					case 1 : if(solucao != null && (melhor == null || solucao.tamanho() < melhor.tamanho()))
								melhor = solucao;break;

					case 2 : if(solucao != null && (melhor == null || solucao.tamanho() >= melhor.tamanho()))
								melhor = solucao;break;

					case 3 : if(solucao != null && (melhor == null || solucao.valor(map) > melhor.valor(map)))
								melhor = solucao;;break;

					case 4 : if(solucao != null && (melhor == null || solucao.time(map) < melhor.time(map)))
								melhor = solucao;break;
								
				}
			}

			if(col - 1 >= 0 && map.free(lin, col - 1)){ //verifica a viabilidade de mover-se para esquerda
				map.step(lin, col);
				Solucao solucao = tentativa_e_erro(map, lin, col - 1, criteria);
				map.unstep(lin,col);

				switch(criteria){
					case 1 : if(solucao != null && (melhor == null || solucao.tamanho() < melhor.tamanho()))
								melhor = solucao;break;

					case 2 : if(solucao != null && (melhor == null || solucao.tamanho() >= melhor.tamanho()))
								melhor = solucao;break;

					case 3 : if(solucao != null && (melhor == null || solucao.valor(map) > melhor.valor(map)))
								melhor = solucao;;break;

					case 4 : if(solucao != null && (melhor == null || solucao.time(map) < melhor.time(map)))
								melhor = solucao;break;
								
				}
			}

		if(melhor != null)  melhor.adiciona(lin,col); // adiciona as coordenadas ao vetor de melhor caminho segundo os criterios
		//melhor.vetor(v);
		//melhor.print();
		return melhor;
	}

	public static void passo(Map map, int [] path){ //funcao para dar os passos de um determinado vetor

		int path_index = path[0];
		int lin, col;

		for(int i=1; i<path_index; i+=2){
			
			lin = path[i];
			col = path[i + 1];

			map.step(lin, col);		// marca-se no mapa a posição está sendo ocupada.
		}
	}


	public static int [] findPath(Map map, int criteria){

		int lin, col; // coordenadas (lin, col) da posição atual
		
		// path é um vetor de inteiro usado para guardar as coordenadas do caminho conforme vai sendo calculado.
		// path_index é usado para gerenciar a ocupação deste vetor. O vetor é usado da seguinte forma:
		//
		// path[0] = quantidade de valores efetivamente armazenados no vetor (não necessáriamente coincide com o tamanho real do vetor)
		// path[1] = linha da 1.a coordenada que faz parte do caminho
		// path[2] = coluna da 1.a coordenada que faz parte do caminho
		// path[3] = linha da 2.a coordenada que faz parte do caminho
		// path[4] = coluna da 2.a coordenada que faz parte do caminho
		// ... etc

		int [] path;  
		int path_index;
		Solucao solucao;

		path = new int[(2 * map.getSize())+1];
		path_index = 1;

		lin = map.getStartLin();
		col = map.getStartCol();
		
		// efetivação de um passo
		map.step(lin, col);		// marcamos no mapa que a posição está sendo ocupada.
		path[path_index] = lin;		// adicionamos as coordenadas da posição (lin, col) no path 
		path[path_index + 1] = col;
		path_index += 2;

		if(DEBUG){

			map.print(); 
			System.out.println("---------------------------------------------------------------");
		}

		if(map.finished(lin, col)) return path;

		Solucao melhor = tentativa_e_erro(map, lin, col, criteria);

		melhor.vetor(path);

		passo(map, path);

		return path;
	}

	public static void printSolution(Map map, int [] path){

		// A partir do mapa e do path contendo a solução, imprime a saída conforme especificações do EP.

		int totalItems = 0;
		int totalValue = 0;
		int totalWeight = 0;

		int path_size = path[0];

		double time = time(map, path);

		System.out.println((path_size - 1)/2 + " " + time);

		for(int i = (path_size-2); i > 0; i -= 2){

			int lin = path[i];
			int col = path[i + 1];
			Item item = map.getItem(lin, col);

			System.out.println(lin + " " + col);

			if(item != null){

				totalItems++;
				totalValue += item.getValue();
				totalWeight += item.getWeight();
			}
		}

		//System.out.println("0 0 0");

		System.out.println(totalItems + " " + totalValue + " " + totalWeight);

		for(int i = (path_size-2); i > 0; i -= 2){
			int lin = path[i];
			int col = path[i + 1];
			Item item = map.getItem(lin, col);
			if(item != null){
				System.out.println(lin + " " + col); //imprime as coordenadas onde se encontram itens
			}
		}

		//map.print();
	}

	public static void main(String [] args) throws IOException {
	
		Map map = new Map(args[0]);

		if(DEBUG){ 
			map.print(); 
			System.out.println("---------------------------------------------------------------");
		}

		int criteria = Integer.parseInt(args[1]);
		int [] path = findPath(map, criteria);
		printSolution(map, path);
	}
}