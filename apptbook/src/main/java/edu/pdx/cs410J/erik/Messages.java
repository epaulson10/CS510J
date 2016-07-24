package edu.pdx.cs410J.erik;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String getMappingCount( int count )
    {
        return String.format( "Server contains %d key/value pairs", count );
    }

    public static String formatKeyValuePair( String key, String value )
    {
        return String.format("  %s -> %s", key, value);
    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String ownerNotFound( String ownerName) {
        return String.format("No appointment book exists with \"%s\" as owner", ownerName);
    }

    public static String mappedKeyValue( String key, String value )
    {
        return String.format( "Mapped %s to %s", key, value );
    }

    public static String allMappingsDeleted() {
        return "All mappings have been deleted";
    }
}
