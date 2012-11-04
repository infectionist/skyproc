/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.DataFormatException;
import lev.LChannel;
import lev.LExporter;
import lev.LFlags;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author pc tech
 */
public class LIGH extends MajorRecordNamed {

    static final SubRecordsPrototype LIGHproto = new SubRecordsPrototype(MajorRecordNamed.namedProto) {

	@Override
	protected void addRecords() {
	    after(new ScriptPackage(), Type.EDID);
	    add(new SubData(Type.OBND));
	    add(new SubString(Type.MODL, true));
	    add(new SubData(Type.MODT));
	    add(new DestructionData());
	    reposition(Type.FULL);
	    add(new SubString(Type.ICON, true));
	    add(new SubString(Type.MICO, true));
	    add(new DATA());
	    add(new SubFloat(Type.FNAM));
	    add(new SubForm(Type.SNAM));
	}
    };
    static Type[] types = {Type.LIGH};

    LIGH() {
        super();
        subRecords.prototype = LIGHproto;
    }

    @Override
    Record getNew() {
        return new LIGH();
    }

    @Override
    Type[] getTypes() {
        return types;
    }

    static class DATA extends SubRecord {

        int time = 0;
        int radius = 0;
        int red = 0;
        int blue = 0;
        int green = 0;
        int holder = 0;
        LFlags flags = new LFlags(4);
        float falloff = 0;
        float fov = 0;
        float nearclip;
        float period = 0;
        float intensity = 0;
        float movement = 0;
        int value = 0;
        float weight = 0;

        DATA() {
            super(Type.DATA);
        }

        @Override
        void export(LExporter out, Mod srcMod) throws IOException {
            super.export(out, srcMod);
            out.write(time);
            out.write(radius);
            out.write(red, 1);
            out.write(blue, 1);
            out.write(green, 1);
            out.write(holder, 1);
            out.write(flags.export(), 4);
            out.write(falloff);
            out.write(fov);
            out.write(nearclip);
            out.write(period);
            out.write(intensity);
            out.write(movement);
            out.write(value);
            out.write(weight);
        }

        @Override
        void parseData(LChannel in) throws BadRecord, DataFormatException, BadParameter {
            super.parseData(in);
            time = in.extractInt(4);
            radius = in.extractInt(4);
            red = in.extractInt(1);
            blue = in.extractInt(1);
            green = in.extractInt(1);
            holder = in.extractInt(1);
            flags.set(in.extract(4));
            falloff = in.extractFloat();
            fov = in.extractFloat();
            nearclip = in.extractFloat();
            period = in.extractFloat();
            intensity = in.extractFloat();
            movement = in.extractFloat();
            value = in.extractInt(4);
            weight = in.extractFloat();
        }

        @Override
        SubRecord getNew(Type type) {
            return new DATA();
        }

        @Override
        int getContentLength(Mod srcMod) {
            return 48;
        }

        @Override
        ArrayList<FormID> allFormIDs() {
            ArrayList<FormID> out = new ArrayList<>(0);
            return out;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final DATA other = (DATA) obj;
            if (this.time != other.time) {
                return false;
            }
            if (this.radius != other.radius) {
                return false;
            }
            if (this.red != other.red) {
                return false;
            }
            if (this.blue != other.blue) {
                return false;
            }
            if (this.green != other.green) {
                return false;
            }
            if (this.holder != other.holder) {
                return false;
            }
            if (!Objects.equals(this.flags, other.flags)) {
                return false;
            }
            if (Float.floatToIntBits(this.falloff) != Float.floatToIntBits(other.falloff)) {
                return false;
            }
            if (Float.floatToIntBits(this.fov) != Float.floatToIntBits(other.fov)) {
                return false;
            }
            if (Float.floatToIntBits(this.nearclip) != Float.floatToIntBits(other.nearclip)) {
                return false;
            }
            if (Float.floatToIntBits(this.period) != Float.floatToIntBits(other.period)) {
                return false;
            }
            if (Float.floatToIntBits(this.intensity) != Float.floatToIntBits(other.intensity)) {
                return false;
            }
            if (Float.floatToIntBits(this.movement) != Float.floatToIntBits(other.movement)) {
                return false;
            }
            if (this.value != other.value) {
                return false;
            }
            if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(other.weight)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.time;
            hash = 89 * hash + this.radius;
            hash = 89 * hash + this.red;
            hash = 89 * hash + this.blue;
            hash = 89 * hash + this.green;
            hash = 89 * hash + this.holder;
            hash = 89 * hash + Objects.hashCode(this.flags);
            hash = 89 * hash + Float.floatToIntBits(this.falloff);
            hash = 89 * hash + Float.floatToIntBits(this.fov);
            hash = 89 * hash + Float.floatToIntBits(this.nearclip);
            hash = 89 * hash + Float.floatToIntBits(this.period);
            hash = 89 * hash + Float.floatToIntBits(this.intensity);
            hash = 89 * hash + Float.floatToIntBits(this.movement);
            hash = 89 * hash + this.value;
            hash = 89 * hash + Float.floatToIntBits(this.weight);
            return hash;
        }

    }

    /**
     *
     */
    public enum LIGHFlag {

        /**
         *
         */
        Dynamic,
        /**
         *
         */
        CanBeCarried,
        /**
         *
         */
        Negative,
        /**
         *
         */
        Flicker,
        /**
         *
         */
        Unknown,
        /**
         *
         */
        OffByDefault,
        /**
         *
         */
        FlickerSlow,
        /**
         *
         */
        Pulse,
        /**
         *
         */
        PulseSlow,
        /**
         *
         */
        Spotlight,
        /**
         *
         */
        ShadowSpotlight,
        /**
         *
         */
        ShadowHemisphere,
        /**
         *
         */
        ShadowOmnidirectional,
        /**
         *
         */
        PortalStrict;
    }

    //Get/Set
    /**
     *
     * @param path
     */
    public void setModel(String path) {
        subRecords.setSubString(Type.MODL, path);
    }

    /**
     *
     * @return
     */
    public String getModel() {
        return subRecords.getSubString(Type.MODL).print();
    }

    DATA getDATA() {
	return (DATA) subRecords.get(Type.DATA);
    }

    /**
     *
     * @param flag
     * @param on
     */
    public void set(LIGHFlag flag, boolean on) {
        getDATA().flags.set(flag.ordinal() + 1, on);
    }

    /**
     *
     * @param flag
     * @return
     */
    public boolean get(LIGHFlag flag) {
        return getDATA().flags.get(flag.ordinal() + 1);
    }

    /**
     *
     * @param gold
     */
    public void setValue(int gold) {
        getDATA().value = gold;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return getDATA().value;
    }

    public int getBlue() {
        return getDATA().blue;
    }

    public void setBlue(int blue) {
        this.getDATA().blue = blue;
    }

    public float getFalloff() {
        return getDATA().falloff;
    }

    public void setFalloff(float falloff) {
        this.getDATA().falloff = falloff;
    }

    public float getFov() {
        return getDATA().fov;
    }

    public void setFov(float fov) {
        this.getDATA().fov = fov;
    }

    public int getGreen() {
        return getDATA().green;
    }

    public void setGreen(int green) {
        this.getDATA().green = green;
    }

    public float getIntensity() {
        return getDATA().intensity;
    }

    public void setIntensity(float intensity) {
        this.getDATA().intensity = intensity;
    }

    public float getMovement() {
        return getDATA().movement;
    }

    public void setMovement(float movement) {
        this.getDATA().movement = movement;
    }

    public float getNearclip() {
        return getDATA().nearclip;
    }

    public void setNearclip(float nearclip) {
        this.getDATA().nearclip = nearclip;
    }

    public float getPeriod() {
        return getDATA().period;
    }

    public void setPeriod(float period) {
        this.getDATA().period = period;
    }

    public int getRadius() {
        return getDATA().radius;
    }

    public void setRadius(int radius) {
        this.getDATA().radius = radius;
    }

    public int getRed() {
        return getDATA().red;
    }

    public void setRed(int red) {
        this.getDATA().red = red;
    }

    public int getTime() {
        return getDATA().time;
    }

    public void setTime(int time) {
        this.getDATA().time = time;
    }

    public float getWeight() {
        return getDATA().weight;
    }

    public void setWeight(float weight) {
        this.getDATA().weight = weight;
    }
}