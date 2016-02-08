package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.thoughtworks.xstream.*;
import model.DataParser;


public class XMLDataParser implements DataParser {
	
	private XStream xstream;
	
	public XMLDataParser(XStream serializator){
		this.xstream = serializator;
	}

	@Override
	public Object getData(String url,String nameFile) {
		// TODO Auto-generated method stub
		try {
			
			return  this.xstream.fromXML(new FileInputStream(url+nameFile+".xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("El archivo no pudo ser abierto");
			return null;
		}
	}

	@Override
	public void saveData(String url, String nameFile, Object o) {
		// TODO Auto-generated method stub
		
		try {
			this.xstream.toXML(o, new FileOutputStream(url+nameFile+".xml"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("El directorio ingresado no es valido");
		}
		
	}

	@Override
	public int numberFiles(String directory) {
		// TODO Auto-generated method stub
		
		String[] array;
		File file = new File(directory);
		array = file.list();
		return array.length;
	}
	
}
