package componente.autenticacao;

import java.nio.file.FileSystems;
import java.nio.file.Path;
public class HardwareToken {
	 private static final String AUTH_FILE_NAME = "arquivo.txt";
	    
	    // Método para verificar se o pen drive conectado é autenticado
			public static boolean AunteticarHardwareToken(String penDrivePath) {
				// Cria o caminho completo para o arquivo de autenticação no pendrive
				Path authFilePath = FileSystems.getDefault().getPath(penDrivePath, AUTH_FILE_NAME);
				return authFilePath.toFile().exists();
			}
		}
