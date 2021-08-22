### Labirinto - Algoritmo de Tentativa e Erro

Esse programa tem a funcionalidade de achar caminhos possíveis de um labirinto passado como no arquivo ".txt" de parâmetro, segundo os critérios:

1 : Caminho mais curto (menos passos, sem considerar o tempo)

2 : Caminho mais longo (mais casas)

3 : Caminho mais valioso

4 : Caminho mais curto (menos tempo)

A execução desse programa é dada pelos comandos:

```javac EP2.java (Para a compilar)```

```java EP2 [Nome do Arquivo].txt [Número do critério] (Para executar)```


OBS: vale ressaltar que por ser um programa que utiliza recursividade, há a possibilidade de _StackOverflow_, para contornar pode-se executar a seguinte linha:

```java -Xss(Valor da Memória em megas)M EP2 (Nome do Arquivo).txt (número do critério)```

O arquivo “.txt” deve ser formatado de modo a especificar as dimensões do labirinto, seguido da formatação do mesmo por modo de matriz, seguido pelo número de itens que há nele, a posição onde se encontram, e seus valores, e pesos respectivos. Por exemplo:

```
7 5 		[numero de linhas] [numero de colunas] 7x5 e dimenção
..... 		[linha 0]
.X.X. 		[linha 1]
.X.X. 		[linha 2]
..... 		[linha 3]
.X.X. 		[linha 4]
.X.X.		[linha 5]
..... 		[linha 6]
3 			[número de itens no mapa]
4 0 4 3 	[linha do item 0] [coluna do item 0] [valor do item 0] [peso do item 0]
3 2 1 8 	[linha do item 1] [coluna do item 1] [valor do item 1] [peso do item 1]
2 0 8 6 	[linha do item 3] [coluna do item 3] [valor do item 3] [peso do item 3]
6 2 		[linha da posição de partida] [coluna da posição de partida]
0 2 		[linha do destino] [coluna do destino]
```

Lembrando que por ser uma representação de matriz e vetores, as coordenadas vão do ponto cartesiano _(0,0)_ até o ponto cartesiano (_[numero de linhas]_ - 1, _[numero de colunas]_ - 1), sendo assim uma representação mais visual:

_(veja o arquivo exemplo.txt, caso haja ainda haja dúvidas sobre a formatação)_


