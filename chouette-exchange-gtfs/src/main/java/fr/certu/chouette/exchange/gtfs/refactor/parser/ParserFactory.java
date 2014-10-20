package fr.certu.chouette.exchange.gtfs.refactor.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ParserFactory
{

   public static Map factories = new HashMap();

   protected abstract GtfsParser create(String path) throws IOException;

   public static final GtfsParser build(String path)
         throws ClassNotFoundException, IOException
   {
      String clazz = getClassName(path);
      if (!factories.containsKey(clazz))
      {
         Class.forName(clazz);
         if (!factories.containsKey(clazz))
            throw new ClassNotFoundException(clazz);
      }
      return ((ParserFactory) factories.get(clazz)).create(path);
   }

   private static String getClassName(String path)
   {
      File fd = new File(path);
      String name = fd.getName();
      name = name.substring(0, name.lastIndexOf('.'));
      String array[] = name.split("_");
      StringBuffer result = new StringBuffer(ParserFactory.class.getPackage()
            .getName());
      result.append('.');
      for (int i = 0; i < array.length; i++)
      {
         char[] buffer = new char[array[i].length()];
         array[i].toLowerCase().getChars(0, array[i].length(), buffer, 0);
         ;
         buffer[0] = Character.toUpperCase(buffer[0]);
         result.append(buffer);
      }
      result.append("Parser");
      return result.toString();
   }
}