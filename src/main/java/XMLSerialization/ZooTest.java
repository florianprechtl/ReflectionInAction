package XMLSerialization;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

public class ZooTest {
	public static void main( String[] args ) {
		Animal panda1 = new Animal( "Tian Tian",
			"male",
			"Ailuropoda melanoleuca",
			271 );
		Animal panda2 = new Animal( "Mei Xiang",
			"female",
			"Ailuropoda melanoleuca",
			221 );
		Zoo national = new Zoo( "National Zoological Park",
			"Washington, D.C." );
		national.add( panda1 );
		national.add( panda2 );
		try {
			XMLOutputter out = new XMLOutputter("\t",true);
			Document d = Driver.serializeObject( national );
			out.output(d, System.out);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}