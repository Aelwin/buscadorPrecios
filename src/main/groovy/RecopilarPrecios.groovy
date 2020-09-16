import groovyx.net.http.*
import java.text.SimpleDateFormat

public class RecopilarPrecios {

	private final String ENCODING = "UTF-8"
	private static List datosRyzen5 = []
	private static List datosRyzen7 = []
	private static List datosB550F = []
	private static List datosSilentBase601 = []
	private static List datosPureRock2 = []
	private static List datosSabrenRocketQ = []

	public static void main(String...args) {
		//Por alguna razón que desconozco el formateo de fechas de groovy no funciona, así que lo hago como en java
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy")		
		datosRyzen5 << formatoFecha.format(new Date())

		def objetosCoolmod = ["datosRyzen5": "https://www.coolmod.com/amd-ryzen-5-3600-wraith-stealth-42ghz-socket-am4-boxed-procesador-precio",
						"datosRyzen7": "https://www.coolmod.com/amd-ryzen-7-3700x-wraith-prism-44ghz-socket-am4-boxed-procesador-precio",
						"datosB550F": "https://www.coolmod.com/asus-rog-strix-b550-f-gaming-socket-am4-placa-base-precio",
						"datosSilentBase601": "https://www.coolmod.com/be-quiet-silent-base-601-plata-caja-torre-precio",
						"datosPureRock2": "https://www.coolmod.com/be-quiet-pure-rock-2-120mm-disipador-cpu-precio"]

		objetosCoolmod.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {
				this[propiedad.name] << recuperarPrecio(objeto.value, { "${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'text-price-total'}}${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'text-price-total-sup'}}" })
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}

		def objetosAmazon = ["datosRyzen5": "https://www.amazon.es/AMD-Ryzen-3600-Procesador-disipador/dp/B07STGGQ18/ref=sr_1_1?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=36QNCGQOA3C07&dchild=1&keywords=ryzen+5+3600&qid=1599820286&sprefix=ryzen%2Caps%2C189&sr=8-1",
							"datosRyzen7": "https://www.amazon.es/AMD-Ryzen-3700X-Procesador-Disipador/dp/B07SXMZLPK/ref=sr_1_1?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=3TA5RJ61PL0J1&dchild=1&keywords=ryzen+7+3700x&qid=1599822100&quartzVehicle=93-1827&replacementKeywords=ryzen+3700x&sprefix=ryzen+7+37%2Caps%2C173&sr=8-1",
							"datosB550F": "https://www.amazon.es/ASUS-ROG-B550-F-generaci%C3%B3n-encabezado/dp/B088W7RKVZ/ref=sr_1_12?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=33UC61X73U8FR&dchild=1&keywords=rog+strix+b550-f+gaming&qid=1599822178&sprefix=rog+strix+b550-f%2Caps%2C170&sr=8-12",
							"datosSilentBase601": "https://www.amazon.es/quiet-Silent-Carcasa-Ordenador-Midi-Tower/dp/B07GT4G988/ref=sr_1_2?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&dchild=1&keywords=silent+base+601&qid=1599822291&sr=8-2",
							"datosPureRock2": "https://www.amazon.es/Ventilador-Adapta-z%C3%B3calos-Cubierta-BK006/dp/B087VM7HT2/ref=sr_1_1?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=2AGROLAH8TSBF&dchild=1&keywords=pure+rock+2&qid=1599822341&quartzVehicle=72-1783&replacementKeywords=pure+rock&sprefix=pure+rock+2%2Caps%2C171&sr=8-1",
							"datosSabrenRocketQ": "https://www.amazon.es/Sabrent-Rocket-Interna-Rendimiento-SB-RKTQ-1TB/dp/B07ZZYWTBP/ref=sr_1_5?__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=1BDY84JQ7YWE&dchild=1&keywords=sabrent+rocket+q&qid=1599822420&sprefix=sabrent+rock%2Caps%2C168&sr=8-5"]

		objetosAmazon.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {
				this[propiedad.name] << recuperarPrecio(objeto.value, { "${it.'**'.find{ elementoHtml -> elementoHtml['@id'] == 'price_inside_buybox'}.text().trim()}" })
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}

		def objetosWipoid = ["datosRyzen5": "https://www.wipoid.com/amd-ryzen-5-3600-36ghz.html",
							"datosRyzen7": "https://www.wipoid.com/amd-ryzen-7-3700x-36ghz.html",
							"datosB550F": "https://www.wipoid.com/asus-rog-strix-b550-f-gaming.html",
							"datosSilentBase601": "https://www.wipoid.com/be-quiet-silent-base-601-silver.html",
							"datosPureRock2": "https://www.wipoid.com/be-quiet-pure-rock-2.html"]
		objetosWipoid.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {
				this[propiedad.name] << recuperarPrecio(objeto.value, { "${it.'**'.find{ elementoHtml -> elementoHtml['@id'] == 'our_price_display'}.text().trim()}" })
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}

		def objetosXtremMedia = ["datosRyzen5": "https://xtremmedia.com/AMD_Ryzen_5_3600_BOX.html",
								"datosRyzen7": "https://xtremmedia.com/AMD_Ryzen_7_3700X_BOX.html",
								"datosB550F": "https://xtremmedia.com/Asus_ROG_STRIX_B550_F_GAMING.html",
								"datosSilentBase601": "https://xtremmedia.com/Be_Quiet_Silent_Base_601_Plata.html",
								"datosPureRock2": "https://xtremmedia.com/Be_Quiet_Pure_Rock_2.html"]
		objetosXtremMedia.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {				
				this[propiedad.name] << recuperarPrecio(objeto.value, { "${it.'**'.find{ elementoHtml -> elementoHtml['@itemprop'] == 'offerDetails'}.text().trim()}" })
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}
		
		def objetosAlternate = ["datosRyzen5": "https://www.alternate.es/html/product/1553392",
								"datosRyzen7": "https://www.alternate.es/html/product/1553396",
								"datosB550F": "https://www.alternate.es/html/product/1647952",
								"datosSilentBase601": "https://www.alternate.es/html/product/1465174",
								"datosPureRock2": "https://www.alternate.es/html/product/1639398"]
		objetosAlternate.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {
				this[propiedad.name] << recuperarPrecio(objeto.value, { "${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'price'}.text().trim()}" })
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}

		def objetosPcComponentes = ["datosRyzen5": "https://www.pccomponentes.com/amd-ryzen-5-3600-36ghz-box",
									"datosRyzen7": "https://www.pccomponentes.com/amd-ryzen-7-3700x-36ghz-box",
									"datosB550F": "https://www.pccomponentes.com/asus-rog-strix-b550-f-gaming",
									"datosSilentBase601": "https://www.pccomponentes.com/be-quiet-silent-base-601-usb-30-plata",
									"datosPureRock2": "https://www.pccomponentes.com/be-quiet-pure-rock-ventilador-cpu"]
		objetosPcComponentes.each { objeto ->
			def propiedad = RecopilarPrecios.getDeclaredFields().find { it.name == objeto.key }
			try {
				this[propiedad.name] << recuperarPrecio(objeto.value, {
					"${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'baseprice'}}${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'cents'}}${it.'**'.find{ elementoHtml -> elementoHtml['@class'] == 'euro'}}"
				})
			} catch(Exception e) {
				this[propiedad.name] << "ERROR"
			}
		}

		String hoja = "Hoja 1!"
		String columna = recuperarColumna()
		SheetsQuickstart.escribirDatos(datosRyzen5, "${hoja}${columna}1:${columna}7")
		SheetsQuickstart.escribirDatos(datosRyzen7, "${hoja}${columna}12:${columna}17")
		SheetsQuickstart.escribirDatos(datosB550F, "${hoja}${columna}22:${columna}27")
		SheetsQuickstart.escribirDatos(datosSilentBase601, "${hoja}${columna}32:${columna}37")
		SheetsQuickstart.escribirDatos(datosPureRock2, "${hoja}${columna}42:${columna}47")
		SheetsQuickstart.escribirDatos(datosSabrenRocketQ, "${hoja}${columna}52")
	}

	private static String recuperarPrecio(String url, Closure encontrarPrecio) {		
		def html = new HTTPBuilder(url).get([:])
		encontrarPrecio(html)		
	}

	//Recupera la columna actual en la cual guardar la información y actualiza el fichero con la siguiente columna
	private String recuperarColumna() {
		File properties = new File("propiedades.txt")
		String columna = properties.getText()
		if (columna == "Z") { properties.write "AA" }
		else { properties.write columna.next() }
		columna
	}
}
