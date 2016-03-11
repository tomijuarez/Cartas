package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.*;

/*Clase para escribir y leer los archivos usando XStream para manejar la informacion*/
public class XMLDataParser implements DataParser {
	
	private XStream xstream;
	
	public XMLDataParser(XStream serializator){
		this.xstream = serializator;
	}

	/*Obbtener data del archivo*/
	@Override
	public Object getData(String url,String nameFile) {
		try {
			
			return  this.xstream.fromXML(new FileInputStream(url+nameFile+".xml"));
		} catch (FileNotFoundException e) {
			System.out.println("El archivo no pudo ser abierto");
			return null;
		}
	}

	/*Guardar data en el archivo*/
	@Override
	public void saveData(String url, String nameFile, Object o) {
		try {
			this.xstream.toXML(o, new FileOutputStream(url+nameFile+".xml"));
		} catch (FileNotFoundException e) {
			System.out.println("El directorio ingresado no es valido");
		}
		
	}

	/*Averiguar cantidad de archivos en un directorio*/
	@Override
	public int numberFiles(String directory) {
		String[] array;
		File file = new File(directory);
		array = file.list();
		if(array != null){
			return array.length;
		}
		else{
			return 0;
		}

	}
	
}
