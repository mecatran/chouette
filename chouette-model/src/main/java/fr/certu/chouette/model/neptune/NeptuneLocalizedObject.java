/**
 * Projet CHOUETTE
 *
 * ce projet est sous license libre
 * voir LICENSE.txt pour plus de details
 *
 */
package fr.certu.chouette.model.neptune;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import fr.certu.chouette.model.neptune.type.LongLatTypeEnum;
import fr.certu.chouette.util.Coordinate;
import fr.certu.chouette.util.CoordinateUtil;

/**
 * Abstract object used for all Localized Neptune Object
 * <p/>
 * Note for fields comment : <br/>
 * when readable is added to comment, a implicit getter is available <br/>
 * when writable is added to comment, a implicit setter is available
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Log4j
public abstract class NeptuneLocalizedObject extends NeptuneIdentifiedObject
{

   @Getter
   @Setter
   @Column(name = "longitude", precision = 19, scale = 16)
   private BigDecimal longitude;

   @Getter
   @Setter
   @Column(name = "latitude", precision = 19, scale = 16)
   private BigDecimal latitude;

   @Getter
   @Setter
   @Enumerated(EnumType.STRING)
   @Column(name = "long_lat_type")
   private LongLatTypeEnum longLatType;

   @Getter
   @Setter
   @Transient
   private BigDecimal x;

   @Getter
   @Setter
   @Transient
   private BigDecimal y;

   @Getter
   @Setter
   @Transient
   private String projectionType;

   @Getter
   @Column(name = "country_code")
   private String countryCode;
   public void setCountryCode(String value)
   {
      countryCode = dataBaseSizeProtectedValue(value,"countryCode",log);
   }

   @Getter
   @Column(name = "zip_code")
   private String zipCode;
   public void setZipCode(String value)
   {
      zipCode = dataBaseSizeProtectedValue(value,"zipCode",log);
   }

   @Getter
   @Column(name = "city_name")
   private String cityName;
   public void setCityName(String value)
   {
      cityName = dataBaseSizeProtectedValue(value,"cityName",log);
   }

   @Getter
   @Column(name = "street_name")
   private String streetName;
   public void setStreetName(String value)
   {
      streetName = dataBaseSizeProtectedValue(value,"streetName",log);
   }

   public abstract String getName();

   public boolean hasCoordinates()
   {
      return longitude != null && latitude != null && longLatType != null;
   }

   public boolean hasAddress()
   {
      return notEmptyString(countryCode) || notEmptyString(streetName);
   }

   public boolean hasProjection()
   {
      return x != null && x != null && notEmptyString(projectionType);
   }

   private boolean notEmptyString(String data)
   {
      return data != null && !data.isEmpty();
   }

   @Override
   public String toString(String indent, int level)
   {
      String s = super.toString(indent, level);
      StringBuffer sb = new StringBuffer(s);
      if (streetName != null && !streetName.isEmpty())
         sb.append("\n").append(indent).append("  streetName = ")
               .append(streetName);
      if (countryCode != null && !countryCode.isEmpty())
         sb.append("\n").append(indent).append("  countryCode = ")
               .append(countryCode);
      if (zipCode != null && !zipCode.isEmpty())
         sb.append("\n").append(indent).append("  zipCode = ").append(zipCode);
      if (cityName != null && !cityName.isEmpty())
         sb.append("\n").append(indent).append("  cityName = ")
               .append(cityName);
      sb.append("\n").append(indent).append("  longLatType = ")
            .append(longLatType);
      sb.append("\n").append(indent).append("  latitude = ").append(latitude);
      sb.append("\n").append(indent).append("  longitude = ").append(longitude);
      if (x != null)
         sb.append("\n").append(indent).append("  x = ").append(x);
      if (y != null)
         sb.append("\n").append(indent).append("  y = ").append(y);
      if (projectionType != null && !projectionType.isEmpty())
         sb.append("\n").append(indent).append("  projection = ")
               .append(projectionType);

      return sb.toString();
   }

   @Override
   public void complete()
   {
      super.complete();
      if (!hasCoordinates())
         return;

      String projection = null;
      if (projectionType == null || projectionType.isEmpty())
      {
         projection = Coordinate.LAMBERT;
      } else
      {
         projection = projectionType.toUpperCase();
         if (projection.equals("WGS84"))
         {
            projection = Coordinate.WGS84;
         }
      }
      Coordinate p = new Coordinate(longitude, latitude);
      Coordinate coordinate = CoordinateUtil.transform(Coordinate.WGS84,
            projection, p);
      if (coordinate != null)
      {
         x = coordinate.x;
         y = coordinate.y;
      }
   }

   public void toLatLong()
   {
      if (!hasProjection())
         return;

      String projection = null;
      if (projectionType == null || projectionType.isEmpty())
      {
         projection = Coordinate.LAMBERT;
      } else
      {
         projection = projectionType.toUpperCase();
         if (projection.equals("WGS84"))
         {
            projection = Coordinate.WGS84;
         }
      }
      Coordinate p = new Coordinate(x, y);
      Coordinate coordinate = CoordinateUtil.transform(projection,
            Coordinate.WGS84, p);
      if (coordinate != null)
      {
         longitude = coordinate.x;
         latitude = coordinate.y;
      }
   }

}