package parte1;

import java.util.ArrayList;
import java.util.Scanner;

import parte2.Casilla;
import parte2.GestorGui;

public class Juego {
	
	// Variables Globales Static Constantes:
	public static final int NUMERO_MINAS_LIMITE_INFERIOR = 40;
	public static final int NUMERO_MINAS_LIMITE_SUPERIOR = 90;
	
	public static final int NUMERO_FILAS_LIMITE_INFERIOR = 10;
	public static final int NUMERO_FILAS_LIMITE_SUPERIOR = 20;
	
	public static final int NUMERO_COLUMNAS_LIMITE_INFERIOR = 15;
	public static final int NUMERO_COLUMNAS_LIMITE_SUPERIOR = 25;
	
	public static final int NUMERO_VIDAS = 3;
	
	public static final char CASILLA_TAPADA = '#';
	public static final char CASILLA_MINA_MARCADA = 'M';
	public static final char CASILLA_MINA_EXPLOTADA = 'X';
	public static final char CASILLA_MINA_OCULTA = 'H';
	public static final char CASILLA_MINA_OCULTA_Y_MARCADA = 'J';
	
	// Variables Globales Static:
	public static boolean juegoActivo;
	public static boolean partidaGanada;
	public static boolean salir;
	
	public static char[][] tableroDeJuego;
	
	public static int coordenadaXActual;
	public static int coordenadaYActual;
	
	public static int numeroDeMinasLimiteInferior;
	public static int numeroDeMinasLimiteSuperior;
	
	public static int numeroDeMinas;
	public static int numeroDeFilas;
	public static int numeroDeColumnas;
	public static int numeroDeVidas;
	
	public static int numeroActualDeMinas;
	public static int numeroActualDeVidas;
	public static int numeroActualDeCasillasTapadas;
	public static int numeroActualDeMinasExplotadas;

	// Metodos Static
	public static void main(String[] args) {
	
		salir = false;
		
		// Mientras el jugador no salga se continuar� jugando
		while (!salir) {
			
			juegoActivo = true;
			partidaGanada = false;
			
			prepararPartida();
			comenzarPartida();
		}

	}
	
	//Metodo que prepara la partida
	public static void prepararPartida() {
		
		//Invoca el metodo preguntar modo de juego y guarda el valor devuelto en un int
		int modoDeJuego = preguntarModoDeJuego();
		
		//Dependiendo del valor devuelto se inicia el juego aleatorio, por teclado, o se sale
		switch (modoDeJuego) {
			case 1:
				iniciarJuegoAleatorio();
				pintarTableroActual();
				break;
			case 2:
				iniciarJuegoPorTeclado();
				pintarTableroActual();
				break;
			case 3:
				juegoActivo = false;
				salir = true;
				System.out.println();
				System.out.println("Saliendo del juego...");
				break;
		}

	}

	//Metodo que pregunta el modo de juego
	public static int preguntarModoDeJuego() {
		Scanner teclado = new Scanner(System.in);
		
		String respuesta;
		int modo = 0;
		boolean valido = false;
		
		// Para continuar debe introducir una opcion valida
		while (!valido) {
			System.out.println();
			System.out.println("Por favor, seleccione la opcion que desea ejecutar y pulse Enter.");
			System.out.println();
			System.out.println("1. Configuracion del estado inicial del juego totalmente aleatorio.");
			System.out.println("2. Configuracion del estado inicial del juego guiado por el jugador.");
			System.out.println("3. Salir de la aplicacion.");
			System.out.println();
			System.out.println("Formato: <opcion> [datos requeridos por la opci�n seleccionada]");
			System.out.println("Ejemplo: 3");
			respuesta = teclado.next();
			
			// Comprobamos que la opcion es correcta
			if (respuesta.equals("1")) {
				modo = 1;
				valido = true;
			} else if (respuesta.equals("2")) {
				modo = 2;
				valido = true;
			} else if (respuesta.equals("3")) {
				modo = 3;
				valido = true;
			} else {
				System.out.println();
				System.out.println("Opcion no valida, seleccione de nuevo.");
			}
		}

		return modo;
	}
	
	//Metodo del juego aleatorio
	public static void iniciarJuegoAleatorio() {
		numeroDeMinas = NUMERO_MINAS_LIMITE_INFERIOR + (int) (Math.random() * ((NUMERO_MINAS_LIMITE_SUPERIOR - NUMERO_MINAS_LIMITE_INFERIOR) + 1));
		numeroDeFilas = NUMERO_FILAS_LIMITE_INFERIOR + (int) (Math.random() * ((NUMERO_FILAS_LIMITE_SUPERIOR - NUMERO_FILAS_LIMITE_INFERIOR) + 1));
		numeroDeColumnas = NUMERO_COLUMNAS_LIMITE_INFERIOR + (int) (Math.random() * ((NUMERO_COLUMNAS_LIMITE_SUPERIOR - NUMERO_COLUMNAS_LIMITE_INFERIOR) + 1));
		numeroDeVidas = NUMERO_VIDAS;
		
		// Inicializamos el tablero de juego
		tableroDeJuego = new char [numeroDeFilas][numeroDeColumnas];
		
		//Bucle para llenar la matriz de casillas tapadas
		for (int i = 0; i < tableroDeJuego.length; i++) {
			for (int j = 0; j < tableroDeJuego[i].length; j++) {
				tableroDeJuego[i][j] = CASILLA_TAPADA;
			}
		}
		
		int minasPorColocar = numeroDeMinas;
		int fil, col;
		
		// Colocamos las minas en diferentes casillas al azar
		while (minasPorColocar != 0) {
			fil = (int) (Math.random() * numeroDeFilas);
			col = (int) (Math.random() * numeroDeColumnas);
			
			// Comprobamos que no hay ya una mina colocada
			if (tableroDeJuego[fil][col] != CASILLA_MINA_OCULTA) {
				tableroDeJuego[fil][col] = CASILLA_MINA_OCULTA;
				minasPorColocar--;
			}
		}
		
		numeroActualDeMinasExplotadas = 0;
		numeroActualDeMinas = numeroDeMinas;
		numeroActualDeVidas = numeroDeVidas;
		numeroActualDeCasillasTapadas = numeroDeFilas * numeroDeColumnas;
	}
	
	//Metodo del juego por teclado
	public static void iniciarJuegoPorTeclado() {
		Scanner teclado = new Scanner(System.in);
		
		String respuesta;
		boolean valido = false;
		
		// Preguntamos el numero de vidas
		int numVidas = 0;
		
		while (!valido) {
			System.out.println("Indique el numero de vidas de la partida y pulse Enter.");
			respuesta = teclado.nextLine();
			respuesta = respuesta.trim();
			System.out.println();
			
			// Comprobamos que la respuesta introducida es un entero positivo
			try {
				numVidas = Integer.parseInt(respuesta);
				
				if (numVidas > 0) {
					numeroDeVidas = numVidas;
					valido = true;
				} else {
					System.out.println("No ha introducido un numero positivo. Vuelva a intentarlo.");
					System.out.println();
				}
			} catch (NumberFormatException errorInt) {
				System.out.println("No ha introducido un numero entero. Vuelva a intentarlo.");
				System.out.println();
			}
		}
		
		// Reseteamos estas variables para una nueva operacion
		valido = false;
		respuesta = "";
		
		// Preguntamos el numero de filas y columnas
		String  filString, colString;
		int fil, col, posAsterisco;
		
		//Mediante el while y el try-catch controlamos los posibles errores
		while (!valido) {
			System.out.println("Indique el tama�o del tablero de juego separando el numero de filas y columnas mediante el simbolo asterisco y pulse Enter.");
			respuesta = teclado.nextLine();
			System.out.println();
			
			respuesta = respuesta.trim();
			
			//Cogemos los numeros que estan antes y despues del asterisco
			posAsterisco = respuesta.indexOf('*');
			
			if(posAsterisco != -1) {
				filString = respuesta.substring(0, posAsterisco);
				colString = respuesta.substring((posAsterisco + 1));
				
				filString = filString.trim();
				colString = colString.trim();
				
				// Comprobamos que son numeros enteros positivos
				try {
					fil = Integer.parseInt(filString);
					col = Integer.parseInt(colString);
					
					if (fil > 0 && col > 0) {
						numeroDeFilas = fil;
						numeroDeColumnas = col;
						valido = true;
					} else {
						System.out.println("No han introducido numeros positivos. Vuelva a intentarlo.");
						System.out.println();
					}
				} catch (NumberFormatException errorInt) {
					System.out.println("No ha introducido numeros enteros. Vuelva a intentarlo.");
					System.out.println();
				}
			} else {
				System.out.println("No ha introducido un formato valido. Vuelva a intentarlo.");
				System.out.println();
			}
			
		}
		
		// Reseteamos estas variables para una nueva operacion
		valido = false;
		respuesta = "";
		
		// Preguntamos por el rango de minas
		String minString, maxString;
		int min, max, posGuion;
		
		while (!valido) {
			System.out.println("Indique el rango de minas separando los dos valores, los cuales deben ser numeros enteros, mediante el siombolo guion y pulse Enter.");
			respuesta = teclado.nextLine();
			System.out.println();
			
			//Cogemos los numeros que estan antes y despues del guion
			posGuion = respuesta.indexOf('-');
			
			if (posGuion != -1) {
				minString = respuesta.substring(0, posGuion);
				maxString = respuesta.substring((posGuion + 1));
				
				minString = minString.trim();
				maxString = maxString.trim();
				
				// Comprobamos que son numeros enteros positivos y que el rango de minas no supere el numero total de espacios
				try {
					min = Integer.parseInt(minString);
					max = Integer.parseInt(maxString);
					
					if (min > 0 && max >= min) {
						if (max < (numeroDeFilas * numeroDeColumnas)) {
							numeroDeMinasLimiteInferior = min;
							numeroDeMinasLimiteSuperior = max;
							numeroDeMinas = numeroDeMinasLimiteInferior + (int) (Math.random() * ((numeroDeMinasLimiteSuperior - numeroDeMinasLimiteInferior) + 1));
							valido = true;
						} else {
							System.out.println("El numero maximo no puede ser superior o igual al numero total de casillas del tablero. Vuelva a intentarlo.");
							System.out.println();
						}
					} else {
						System.out.println("No ha introducido numeross validos. Vuelva a intentarlo.");
						System.out.println();
					}
				} catch (NumberFormatException e) {
					System.out.println("No ha introducido numeros enteros. Vuelva a intentarlo.");
					System.out.println();
				}
			} else {
				System.out.println("No ha introducido un formato valido. Vuelva a intentarlo.");
				System.out.println();
			}
			
		}
		
		// Inicializamos el tablero de juego
		tableroDeJuego = new char [numeroDeFilas][numeroDeColumnas];
		
		//Bucle que pone todas las posiciones de la matriz con casillas tapadas 
		for (int i = 0; i < tableroDeJuego.length; i++) {
			for (int j = 0; j < tableroDeJuego[i].length; j++) {
				tableroDeJuego[i][j] = CASILLA_TAPADA;
			}
		}
		
		int minasPorColocar = numeroDeMinas;
		
		// Colocamos las minas en diferentes casillas al azar
		while (minasPorColocar != 0) {
			fil = (int) (Math.random() * numeroDeFilas);
			col = (int) (Math.random() * numeroDeColumnas);
			
			// Comprobamos que no hay ya una mina colocada
			if (tableroDeJuego[fil][col] != CASILLA_MINA_OCULTA) {
				tableroDeJuego[fil][col] = CASILLA_MINA_OCULTA;
				minasPorColocar--;
			}
		}
		
		numeroActualDeMinasExplotadas = 0;
		numeroActualDeMinas = numeroDeMinas;
		numeroActualDeVidas = numeroDeVidas;
		numeroActualDeCasillasTapadas = numeroDeFilas * numeroDeColumnas;
	}

	//Metodo para pintar el tablero
	public static void pintarTableroActual() {
		System.out.println();
		System.out.println("****************************");
		System.out.println("*** Vidas restantes = " + numeroActualDeVidas + "  ***");
		System.out.println("*** Minas restantes = " + numeroActualDeMinas + " ***");
		System.out.println("****************************");
		System.out.println();
		
		//Genera la 1 fila de asteriscos que rodea el tablero 
		for (int i = 0; i < tableroDeJuego[0].length + 2; i++) {
			System.out.print("*");
		}
		System.out.println();
		
		//Genera el tablero con la y ultima columna llena de asteriscos 
		for (int i = 0; i < tableroDeJuego.length; i++) {
			System.out.print("*");
			for (int j = 0; j < tableroDeJuego[i].length; j++) {
				//Pintar casilla tapada si hay una mina oculta
				if (tableroDeJuego[i][j] == CASILLA_MINA_OCULTA && !partidaGanada) {
					System.out.print(CASILLA_TAPADA);
					//Pintar una casilla marcada si hay una mina oculta y esta marcada
				} else if (tableroDeJuego[i][j] == CASILLA_MINA_OCULTA_Y_MARCADA && !partidaGanada) {
					System.out.print(CASILLA_MINA_MARCADA);
				} else {
					System.out.print(tableroDeJuego[i][j]);
				}
			}
			System.out.print("*");
			System.out.println();
		}
		//Genera la ultima fila de asteriscos que rodea el tablero 
		for (int i = 0; i < tableroDeJuego[0].length + 2; i++) {
			System.out.print("*");
		}
		System.out.println();
	}

	//Metodo para preguntar la accion de juego
	public static int preguntarAccionDeJuego() {
		Scanner teclado = new Scanner(System.in);

		String respuesta, accionString, filString, colString;
		int accion = 0, fil, col, posAsterisco, posEspacio;
		boolean valido = false;
		
		//El while nos permite volver ha preguntar hasta que introduzca los datos en el formato adecuado
		while(!valido) {
			System.out.println();
			System.out.println("Por favor, seleccione la accion que desea ejecutar y pulse Enter.");
			System.out.println();
			System.out.println("1. Descubrir casilla, indicando fila y columna separando los valores mediante asterisco.");
			System.out.println("2. Marcar una mina, indicando fila y columna separando los valores mediante asterisco.");
			System.out.println("3. Desmarcar una mina, indicando fila y columna separando los valores mediante asterisco.");
			System.out.println("4. Comenzar nuevo juego.");
			System.out.println("5. Salir.");
			System.out.println();
			System.out.print("Formato: <opcion> [datos requeridos por la opcionn seleccionada].");
			System.out.println("Ejemplo: 1 4*5");
			respuesta = teclado.nextLine();
			System.out.println();
			
			respuesta = respuesta.trim();
			
			posAsterisco = respuesta.indexOf('*');
			posEspacio = respuesta.indexOf(' ');
			
			// Opciones 1 2 y 3 con el formato '1 4*5'
			if(posAsterisco != -1 && posEspacio != -1) {
				accionString = respuesta.substring(0, posEspacio);
				filString = respuesta.substring(posEspacio + 1, posAsterisco);
				colString = respuesta.substring((posAsterisco + 1));
				
				// Comprobamos que son numeros enteros positivos
				try {
					accion = Integer.parseInt(accionString);
					fil = Integer.parseInt(filString);
					col = Integer.parseInt(colString);
					
					if (fil > 0 && col > 0) {
						if (accion == 1 || accion == 2 || accion == 3) {
							coordenadaXActual = fil;
							coordenadaYActual = col;
							valido = true;
						} else {
							System.out.println("No ha introducido una opci�n v�lida. Vuelva a intentarlo.");
							System.out.println();
						}
					} else {
						System.out.println("No ha introducido n�meros positivos. Vuelva a intentarlo.");
						System.out.println();
					}
				} catch (NumberFormatException e) {
					System.out.println("No ha introducido n�meros enteros. Vuelva a intentarlo.");
					System.out.println();
				}
			} else if(posEspacio == -1 && posAsterisco == -1) { // Opcion 4 y 5
				try {
					accion =  Integer.parseInt(respuesta);
					
					if (accion == 4 || accion == 5) {
						valido = true;
					} else {
						System.out.println("No ha introducido una opcion valida. Vuelva a intentarlo.");
						System.out.println();
					}
				} catch (NumberFormatException e) {
					System.out.println("No ha introducido numeros enteros. Vuelva a intentarlo.");
					System.out.println();
				}
			} else {
				System.out.println("No ha introducido un formato valido. Vuelva a intentarlo.");
				System.out.println();
			}
		}
		
		return accion;
	}

	//Metodo para ejecutar las acciones
	public static String ejecutarAccion(int accionDeJuego) {
		
		String mensaje = "";
		
		//Los - 1 se ponen porque las matrices empiezan en 0
		switch (accionDeJuego) {
			case 1:
				mensaje = descubrirCasilla((coordenadaXActual - 1), (coordenadaYActual - 1));
				break;
			case 2:
				mensaje = marcarMina((coordenadaXActual - 1), (coordenadaYActual - 1));
				break;
			case 3:
				mensaje = desmarcarMina((coordenadaXActual - 1), (coordenadaYActual - 1));
				break;
			case 4:
				juegoActivo = false;
				break;
			case 5:
				juegoActivo = false;
				salir = true;
				System.out.println("Saliendo del juego...");
				break;
		}
		
		return mensaje;
	}
	
	//Metodo para comenzar la partida
	public static void comenzarPartida() {
		Scanner teclado = new Scanner(System.in);
		
		int accion;
		String mensaje = "";
		
		while(juegoActivo) {
			
			//if (!partidaGanada) {
				accion = preguntarAccionDeJuego();
				mensaje = ejecutarAccion(accion);
			//}
			
			if (juegoActivo) {
				pintarTableroActual();
			}
			
			if (!mensaje.equals("")) {
				System.out.println();
				System.out.println(mensaje);
				System.out.println();
			}
			
			if (partidaGanada) {
				System.out.println("Pulse cualquier tecla y despues enter para volver al menu principal.");
				teclado.next();
				System.out.println();
				
				juegoActivo = false;
			}
			
			
		}
		
	}
	
	//Metodo para comprobar si la casilla es valida
	public static boolean casillaValida(int x, int y) {
		boolean valida = false;
		
		if (x >= 0 && x < numeroDeFilas && y >= 0 && y < numeroDeColumnas) {
			valida = true;
		}
		
		return valida;
	}
	
	//Metodo para ver el estado de las minas
	public static int evaluarMinaEnCasilla(int x, int y) {
		int hayMina = 0;
		
		if (casillaValida(x, y)) {
			if (tableroDeJuego[x][y] == CASILLA_MINA_EXPLOTADA || tableroDeJuego[x][y] == CASILLA_MINA_MARCADA || tableroDeJuego[x][y] == CASILLA_MINA_OCULTA || tableroDeJuego[x][y] == CASILLA_MINA_OCULTA_Y_MARCADA) {
				hayMina = 1;
			}
		}
		
		return hayMina;
	}
	
	//Metodo para saber si hay una mina oculta sin marcar
	public static boolean casillaTapadaSinMinaMarcada(int x, int y) {
		boolean minaNoVisible = false;
		
		if (casillaValida(x, y)) {
			if (tableroDeJuego[x][y] == CASILLA_TAPADA || tableroDeJuego[x][y] == CASILLA_MINA_OCULTA) {
				minaNoVisible = true;
			}
		}
		
		return minaNoVisible;
	}
	
	//Metodo para saber si hay una mina marcada
	public static boolean casillaConMinaMarcada(int x, int y) {
		boolean marcada = false;
		
		if (casillaValida(x, y)) {
			if (tableroDeJuego[x][y] == CASILLA_MINA_MARCADA || tableroDeJuego[x][y] == CASILLA_MINA_OCULTA_Y_MARCADA) {
				marcada = true;
			}
		}
		
		return marcada;
	}
	
	//metodo para ver el estado de una casilla (tapada o no)
	public static boolean casillaTapadaSinMinaOcultaNiMarcada(int x, int y) {
		boolean tapadaSinMina = false;
		
		if (casillaValida(x, y)) {
			if (tableroDeJuego[x][y] == CASILLA_TAPADA) {
				tapadaSinMina = true;
			}
		}
		
		return tapadaSinMina;
	}
	
	//Metodo para marcar la mina
	public static String marcarMina(int x, int y) {
		String mensaje = "";
		
		if (casillaValida(x, y)) {
			if (casillaTapadaSinMinaMarcada(x, y)) {
				numeroActualDeMinas--;
				tableroDeJuego[x][y] = CASILLA_MINA_MARCADA;
			} else if (tableroDeJuego[x][y] == CASILLA_MINA_OCULTA) {
				tableroDeJuego[x][y] = CASILLA_MINA_OCULTA_Y_MARCADA;
			} else {
				if(tableroDeJuego[x][y] == CASILLA_MINA_EXPLOTADA) {
					mensaje = "ERROR: Casilla ya explotada.";
				} else {
					mensaje = "ERROR: Casilla ya marcada.";
				}
			}
		} else {
			mensaje = "ERROR: Las coordenadas indicadas no son correctas.";
		}
		
		return mensaje;
	}
	
	//Metodo para desmarcar una casilla
	public static String desmarcarMina(int x, int y) {
		String mensaje = "";
		
		if (casillaValida(x, y)) {
			if (casillaConMinaMarcada(x, y)) {
				numeroActualDeMinas++;
				if(tableroDeJuego[x][y] == CASILLA_MINA_OCULTA_Y_MARCADA) {
					tableroDeJuego[x][y] = CASILLA_MINA_OCULTA;
				} else {
					tableroDeJuego[x][y] = CASILLA_TAPADA;
				}
			} else {
				if(tableroDeJuego[x][y] == CASILLA_MINA_EXPLOTADA) {
					mensaje = "ERROR: Casilla ya explotada.";
				} else {
					mensaje = "ERROR: Casilla ya desmarcada.";
				}
			}
		} else {
			mensaje = "ERROR: Las coordenadas indicadas no son correctas.";
		}
		
		return mensaje;
	}
	
	//Metodo para descubrir una casilla
	public static String descubrirCasilla(int x, int y) {
		String mensaje = "";
		
		if (casillaValida(x, y)) {
			if (casillaTapadaSinMinaMarcada(x, y)) {
				if (tableroDeJuego[x][y] == CASILLA_MINA_OCULTA) { //Si hay una mina oculta
					numeroActualDeVidas--;
					numeroActualDeMinas--;
					numeroActualDeCasillasTapadas--;
					numeroActualDeMinasExplotadas++;
					tableroDeJuego[x][y] = CASILLA_MINA_EXPLOTADA;
					
					if (numeroActualDeVidas ==  0) {
						mensaje = "No te quedan vidas, has perdido.";
						juegoActivo = false;
					}
					
				} else { // si no hay una mina oculta
					
					ArrayList<String> casillasAdicionales = new ArrayList<String>();
					casillasAdicionales.add(x + "-" + y);
					
					while (!casillasAdicionales.isEmpty()) {
						String casillaActual = casillasAdicionales.get(0);
						casillasAdicionales.remove(0);
						int posGuion = casillaActual.indexOf('-');
						int casillaActualX = Integer.parseInt(casillaActual.substring(0, posGuion));
						int casillaActualY = Integer.parseInt(casillaActual.substring((posGuion + 1)));
						
						int contadorDeMinasColindantes = 0;
						
						if (casillaTapadaSinMinaOcultaNiMarcada(casillaActualX, casillaActualY)) {
							for (int i = -1; i <= 1; i++) {
								for (int j = -1; j <= 1; j++) {
									if (!(i == 0 && j == 0) && casillaValida((casillaActualX + i), (casillaActualY + j))) {
										contadorDeMinasColindantes = contadorDeMinasColindantes + evaluarMinaEnCasilla((casillaActualX + i), (casillaActualY + j));
									}
								}
							}
						
							if (contadorDeMinasColindantes > 0) {
								numeroActualDeCasillasTapadas--;
								tableroDeJuego[casillaActualX][casillaActualY] = Character.forDigit(contadorDeMinasColindantes, 10);
							} else {
								tableroDeJuego[casillaActualX][casillaActualY] = ' ';
								numeroActualDeCasillasTapadas--;
								
								for (int i = -1; i <= 1; i++) {
									for (int j = -1; j <= 1; j++) {
										if (!(i == 0 && j == 0) && casillaValida((casillaActualX + i), (casillaActualY + j))) {
											casillasAdicionales.add((casillaActualX + i) + "-" + (casillaActualY + j));
										}
									}
								}
							}
						}
					}
				}
				
				//Si te quedan vidas y has destapado todas las casillas GANAS
				System.out.println("esto: tapadas=" +numeroActualDeCasillasTapadas + " explotadas=" + numeroActualDeMinasExplotadas + " minas=" + numeroActualDeMinas + " = " + (numeroActualDeCasillasTapadas + numeroActualDeMinasExplotadas - numeroActualDeMinas));
				if (((numeroActualDeCasillasTapadas + numeroActualDeMinasExplotadas - numeroActualDeMinas) == 0) || ((numeroActualDeVidas > 0) && (numeroActualDeMinas == 0))) {
					partidaGanada = true;
					mensaje = "Has ganado el juego?.";
				}
				//Control de errores
			} else {
				mensaje = "ERROR: La casilla ya ha sido marcada o descubierta previamente.";
			}
		} else {
			mensaje = "ERROR: Las coordenadas indicadas no son correctas.";
		}
		
		return mensaje;
	}
	
	//Metodo para limpiar
	public static void limpiar() {
		try{
			final String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				Runtime.getRuntime().exec("cls");
			} else {
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		} catch (final Exception e){
			System.out.println("ERROR al limpiar la consola.");
		}
	}
}
