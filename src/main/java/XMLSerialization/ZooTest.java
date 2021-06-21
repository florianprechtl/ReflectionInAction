package XMLSerialization;

import java.io.File;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

public class ZooTest {
	public static void main( String[] args ) {
		Animal panda1 = new Animal();
		panda1.initialize( "Tian Tian",
				"male",
				"Ailuropoda melanoleuca",
				271 );
		Animal panda2 = new Animal();
		panda2.initialize( "Mei Xiang",
				"female",
				"Ailuropoda melanoleuca",
				221 );
		Zoo national = new Zoo();
		national.initialize( "National Zoological Park",
				"Washington, D.C." );
		national.add( panda1 );
		national.add( panda2 );
		try {
			XMLOutputter out = new XMLOutputter("\t",true);
			Document d = Serializer.serializeObject( national );
			out.output(d, System.out);

			File file = new File("zooXML");
			FileHelper.createFile(file);
			FileHelper.writeToFile(out.outputString(d) , file.getAbsolutePath());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}