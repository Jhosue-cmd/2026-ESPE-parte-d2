package es.upm.grise.profundizacion.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.upm.grise.profundizacion.exceptions.InvalidContentException;
import es.upm.grise.profundizacion.exceptions.WrongFileTypeException;

public class FileTest {
	
	private File file;

	@BeforeEach
	public void setUp() {
		// Se inicializa una nueva instancia de File antes de cada prueba
		file = new File();
	}

	@Test
	@DisplayName("Test 1: La lista de contenido se inicializa correctamente (vacía pero no nula)")
	public void testConstructorInitializesContent() {
		assertNotNull(file.getContent(), "La lista de contenido no debería ser nula");
		assertTrue(file.getContent().isEmpty(), "La lista de contenido debería estar vacía al inicio");
	}

	@Test
	@DisplayName("Test 2: addProperty lanza InvalidContentException si el contenido es null")
	public void testAddPropertyThrowsExceptionIfNull() {
		assertThrows(InvalidContentException.class, () -> {
			file.addProperty(null);
		}, "Debería lanzar InvalidContentException cuando el argumento es null");
	}

	@Test
	@DisplayName("Test 3: addProperty lanza WrongFileTypeException si el tipo es IMAGE")
	public void testAddPropertyThrowsExceptionIfImage() {
		// Configuramos el archivo como tipo IMAGE
		file.setType(FileType.IMAGE);
		
		char[] newContent = {'a', 'b', 'c'};
		
		assertThrows(WrongFileTypeException.class, () -> {
			file.addProperty(newContent);
		}, "Debería lanzar WrongFileTypeException si se intenta añadir propiedad a un archivo de imagen");
	}

	@Test
	@DisplayName("Test 4: addProperty añade contenido correctamente si el tipo es PROPERTY")
	public void testAddPropertyAddsContentCorrectly() throws InvalidContentException, WrongFileTypeException {
		// Configuramos el archivo como tipo PROPERTY
		file.setType(FileType.PROPERTY);
		
		char[] newContent = {'T', 'e', 's', 't'};
		file.addProperty(newContent);
		
		// Verificamos que el tamaño es correcto y el contenido coincide
		assertEquals(4, file.getContent().size(), "El tamaño del contenido debería ser 4");
		assertEquals('T', file.getContent().get(0), "El primer carácter debería ser 'T'");
		assertEquals('t', file.getContent().get(3), "El último carácter debería ser 't'");
	}
	
	@Test
	@DisplayName("Test 5: addProperty funciona correctamente cuando el tipo no está definido (null) pero no es IMAGE")
	public void testAddPropertyWorksWhenTypeIsNull() throws InvalidContentException, WrongFileTypeException {
		// Por defecto type es null, lo cual no es igual a FileType.IMAGE
		char[] newContent = {'1', '2'};
		file.addProperty(newContent);
		
		assertEquals(2, file.getContent().size());
	}
	
	@Test
	@DisplayName("Test 6: getCRC32 devuelve 0 si el contenido está vacío")
	public void testGetCRC32ReturnsZeroIfEmpty() {
		// Sin añadir contenido
		long crc = file.getCRC32();
		assertEquals(0L, crc, "El CRC32 debería ser 0 si el contenido está vacío");
	}

	@Test
	@DisplayName("Test 7: getCRC32 procesa el contenido (Devuelve 0 debido al Stub de FileUtils)")
	public void testGetCRC32WithContent() throws InvalidContentException, WrongFileTypeException {
		// Añadimos contenido para que entre en la lógica de cálculo
		file.setType(FileType.PROPERTY);
		file.addProperty("Data".toCharArray());
		
		// NOTA: Dado que File.java instancia 'new FileUtils()' internamente y 
		// la implementación proporcionada de FileUtils devuelve por defecto 0 (campo CRC32 no inicializado),
		// esperamos 0. Esto verifica que el método se ejecuta sin excepciones.
		long crc = file.getCRC32();
		assertEquals(0L, crc, "El CRC32 devuelve 0 con el stub actual de FileUtils");
	}
}