package junitjndi.types;

public enum TypeJndiEntry
{
	ROOT("java:/"),

	GLOBAL("java:global/"),

	APPLICATION("java:app/"),

	JBOSS("java:jboss/"),

	MODULE("java:module/"),

	COMP("java:comp/");

	private final String jndiEntry;

	TypeJndiEntry(final String jndiEntry)
	{
		this.jndiEntry = jndiEntry;
	}

	public String getJndiEntry()
	{
		return jndiEntry;
	}
}
