package junitjndi.model;

import static org.fest.assertions.api.Assertions.assertThat;

import javax.naming.NamingException;

import org.junit.Test;

import junitjndi.types.TypeJndiEntry;

public class JndiEntryResolverTest
{
	@Test
	public void testConstructorSimpleWithNullValue() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver(null);
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isNull();
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.getResolvedName()).isEqualTo("");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:/");
	}

	@Test
	public void testConstructorSimpleWithEmptyValue() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.getResolvedName()).isEqualTo("");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:/");
	}

	@Test
	public void testConstructorSimpleWithJustCategoryRoot() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("java:/");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("java:/");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.getResolvedName()).isEqualTo("");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:/");
	}

	@Test
	public void testConstructorSimpleWithJustCategoryApplication() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("java:app/");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("java:app/");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.APPLICATION);
		assertThat(resolver.getResolvedName()).isEqualTo("");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:app/");
	}

	@Test
	public void testConstructorSimpleWithSimpleValue() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("allo");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("allo");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.getResolvedName()).isEqualTo("allo");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:/allo");
	}

	@Test
	public void testConstructorSimpleWithSimpleValueAndSlash() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("/allo");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("/allo");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.getResolvedName()).isEqualTo("allo");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:/allo");
	}

	@Test
	public void testConstructorSimpleWithValueAndVisibilityGlobal() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("java:global/bonjour/fabien");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isNull();
		assertThat(resolver.getOriginalName()).isEqualTo("java:global/bonjour/fabien");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.GLOBAL);
		assertThat(resolver.getResolvedName()).isEqualTo("bonjour/fabien");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:global/bonjour/fabien");
	}

	@Test
	public void testConstructorComplexWithAllValues() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("/bravo/navette", TypeJndiEntry.JBOSS, "niv1/niv2");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isEqualTo("niv1/niv2");
		assertThat(resolver.getOriginalName()).isEqualTo("/bravo/navette");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.JBOSS);
		assertThat(resolver.getResolvedName()).isEqualTo("niv1/niv2/bravo/navette");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:jboss/niv1/niv2/bravo/navette");
	}

	@Test
	public void testConstructorComplexWithAllValuesGlobal() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver("java:app/puf", TypeJndiEntry.GLOBAL, "a/b/c/d/e/f");
		assertThat(resolver).isNotNull();
		assertThat(resolver.getOriginalParentName()).isEqualTo("a/b/c/d/e/f");
		assertThat(resolver.getOriginalName()).isEqualTo("java:app/puf");
		assertThat(resolver.getJndiType()).isEqualTo(TypeJndiEntry.GLOBAL);
		assertThat(resolver.getResolvedName()).isEqualTo("a/b/c/d/e/f/puf");
		assertThat(resolver.getFullQualifiedName()).isEqualTo("java:global/a/b/c/d/e/f/puf");
	}

	@Test()
	public void testDetectJndiEntryType() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver(null);

		assertThat(resolver.detectJndiEntryType(null)).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.detectJndiEntryType("")).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.detectJndiEntryType("pof")).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.detectJndiEntryType("java:global")).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.detectJndiEntryType("java:global/")).isEqualTo(TypeJndiEntry.GLOBAL);
		assertThat(resolver.detectJndiEntryType("java:global/allo")).isEqualTo(TypeJndiEntry.GLOBAL);
		assertThat(resolver.detectJndiEntryType("java:app/allo")).isEqualTo(TypeJndiEntry.APPLICATION);
		assertThat(resolver.detectJndiEntryType("java:app/allo/maman/bobo")).isEqualTo(TypeJndiEntry.APPLICATION);
		assertThat(resolver.detectJndiEntryType("java:module/allo/alhuile")).isEqualTo(TypeJndiEntry.MODULE);
		assertThat(resolver.detectJndiEntryType("java:jboss/toc")).isEqualTo(TypeJndiEntry.JBOSS);
		assertThat(resolver.detectJndiEntryType("java:/a/b/c/d")).isEqualTo(TypeJndiEntry.ROOT);
		assertThat(resolver.detectJndiEntryType("java:comp/x/y/z")).isEqualTo(TypeJndiEntry.COMP);
		assertThat(resolver.detectJndiEntryType("java:jboss/zz/qq")).isEqualTo(TypeJndiEntry.JBOSS);
	}

	@Test()
	public void testDetectResolvedName() throws Exception
	{
		final JndiEntryResolver resolver = new MockJndiEntryResolver(null);
		assertThat(resolver.detectResolvedName("allo", TypeJndiEntry.ROOT, null)).isEqualTo("allo");
		assertThat(resolver.detectResolvedName("/allo", TypeJndiEntry.JBOSS, "/level1/level2/level3")).isEqualTo("level1/level2/level3/allo");
		assertThat(resolver.detectResolvedName(null, TypeJndiEntry.MODULE, null)).isEqualTo("");
		assertThat(resolver.detectResolvedName("", TypeJndiEntry.MODULE, null)).isEqualTo("");
		assertThat(resolver.detectResolvedName("", TypeJndiEntry.MODULE, "")).isEqualTo("");
		assertThat(resolver.detectResolvedName("", TypeJndiEntry.MODULE, "abc")).isEqualTo("abc/");
	}

	@Test(expected = NamingException.class)
	public void testValidateJndiNameNull() throws Exception
	{
		new JndiEntryResolver(null);
	}

	@Test(expected = NamingException.class)
	public void testValidateJndiNameEmpty() throws Exception
	{
		new JndiEntryResolver("");
	}

	@Test(expected = NamingException.class)
	public void testValidateJndiNameBlank() throws Exception
	{
		new JndiEntryResolver("  ");
	}

	@Test(expected = NamingException.class)
	public void testValidateWithJndiNameDoubleSlash() throws Exception
	{
		new JndiEntryResolver("//webapp");
	}

	@Test(expected = NamingException.class)
	public void testValidateWithJndiNameEndSlash() throws Exception
	{
		new JndiEntryResolver("/webapp/");
	}

	@Test(expected = NamingException.class)
	public void testValidateWithJndiNameContainDoubleSlash() throws Exception
	{
		new JndiEntryResolver("/web//app");
	}

	@Test()
	public void testValidate() throws Exception
	{
		new JndiEntryResolver("/webapp");
		new JndiEntryResolver("webapp");
		new JndiEntryResolver("java:module/webapp");
		new JndiEntryResolver("java:/webapp");
	}

}
