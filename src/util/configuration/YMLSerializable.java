package util.configuration;

/**
 * Used for serialization of YAML scripts.
 * 
 * @author Albert Beaupre
 * @author Dirk Jamieson
 */
public interface YMLSerializable {

	/**
	 * Takes the specified {@code object} element and creates a serialized {@code ConfigSection} based
	 * on it.
	 * 
	 * @param object
	 *            the object to serialize
	 * @return the serialized {@code ConfigSection} based on the specified object
	 */
	public ConfigSection serialize();

	/**
	 * Deserializes the specified {@code ConfigSection} given based on the element of this
	 * {@code YMLSerializable}.
	 * 
	 * @param section
	 *            the {@code ConfigSection} to take for deserialization
	 * @return the deserialized object
	 */
	public void deserialize(ConfigSection section);

}
