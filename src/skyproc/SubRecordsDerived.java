/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package skyproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import lev.LChannel;
import lev.LExporter;
import lev.LFileChannel;
import lev.Ln;
import skyproc.exceptions.BadParameter;
import skyproc.exceptions.BadRecord;

/**
 *
 * @author Justin Swanson
 */
public class SubRecordsDerived extends SubRecords {

    protected SubRecordsPrototype prototype;
    protected Map<Type, RecordLocation> pos = new HashMap<>(0);
    Mod srcMod;

    public SubRecordsDerived(SubRecordsPrototype proto) {
	this.prototype = proto;
    }

    public SubRecordsDerived(SubRecordsDerived rhs) {
	super(rhs);
	prototype = rhs.prototype;
	srcMod = rhs.srcMod;
	pos = new HashMap(rhs.pos);
    }

    @Override
    protected void export(LExporter out, Mod srcMod) throws IOException {
	for (Type t : prototype.list) {
	    if (shouldExport(t)) {
		SubRecord instance = get(t);
		instance.export(out, srcMod);
	    }
	}
    }

    @Override
    public boolean shouldExport(SubRecord s) {
	return prototype.forceExport.contains(s.getType()) || super.shouldExport(s);
    }

    public boolean shouldExport(Type t) {
	if (map.containsKey(t)) {
	    return shouldExport(map.get(t));
	} else if (pos.containsKey(t)) {
	    SubRecord s = get(t);
	    return shouldExport(s);
	} else {
	    return shouldExport(prototype.get(t));
	}
    }

    @Override
    public boolean contains(Type t) {
	return prototype.contains(t);
    }

    @Override
    public SubRecord get(Type in) {
	SubRecord s = null;
	if (map.containsKey(in)) {
	    s = map.get(in);
	} else if (prototype.contains(in)) {
	    s = createFromPrototype(in);
	    loadFromPosition(s);
	    s.standardize(srcMod);
	    s.fetchStringPointers(srcMod);
	}
	return s;
    }

    SubRecord createFromPrototype(Type in) {
	SubRecord s = prototype.get(in).getNew(in);
	add(s);
	return s;
    }

    void loadFromPosition(SubRecord s) {
	if (SPGlobal.streamMode) {
	    RecordLocation position = pos.get(s.getType());
	    if (position != null) {
		srcMod.input.pos(position.pos);
		try {
		    for (int i = 0; i < position.num; i++) {
			s.parseData(s.extractRecordData(srcMod.input));
		    }
		} catch (DataFormatException | BadRecord | BadParameter ex) {
		    Logger.getLogger(SubRecordsDerived.class.getName()).log(Level.SEVERE, null, ex);
		}
		pos.remove(s.getType());
	    }
	}
    }

    @Override
    void importSubRecord(LChannel in) throws BadRecord, DataFormatException, BadParameter {
	Type nextType = Record.getNextType(in);
	if (contains(nextType)) {
	    if (SPGlobal.streamMode && (in instanceof RecordShrinkArray || in instanceof LFileChannel)) {
		Type standardType = prototype.get(nextType).getType();
		if (!pos.containsKey(standardType)) {
		    long position = in.pos();
		    pos.put(standardType, new RecordLocation(position));
		    if (SPGlobal.logging()) {
			SPGlobal.logSync(nextType.toString(), nextType.toString() + " is at position: " + Ln.printHex(position));
		    }
		} else {
		    pos.get(standardType).num++;
		}
		in.skip(prototype.get(nextType).getRecordLength(in));
	    } else {
		SubRecord record = getSilent(nextType);
		record.parseData(record.extractRecordData(in));
		record.standardize(srcMod);
		record.fetchStringPointers(srcMod);
	    }
	} else {
	    throw new BadRecord("Doesn't know what to do with a " + nextType.toString() + " record.");
	}
    }

    public SubRecord getSilent(Type nextType) {
	if (map.containsKey(nextType)) {
	    return map.get(nextType);
	} else {
	    return createFromPrototype(nextType);
	}
    }

    @Override
    public void remove(Type in) {
	super.remove(in);
	if (pos.containsKey(in)) {
	    pos.remove(in);
	}
    }

    @Override
    public int length(Mod srcMod) {
	int length = 0;
	for (Type t : prototype.list) {
	    SubRecord s = get(t);
	    if (s != null && shouldExport(s)) {
		length += s.getTotalLength(srcMod);
	    }
	}
	return length;
    }

    @Override
    public ArrayList<FormID> allFormIDs() {
	ArrayList<FormID> out = new ArrayList<>();
	for (Type t : prototype.list) {
	    if (shouldExport(t)) {
		SubRecord s = get(t);
		out.addAll(s.allFormIDs());
	    }
	}
	return out;
    }

    protected static class RecordLocation {

	long pos;
	int num = 1;

	RecordLocation(long pos) {
	    this.pos = pos;
	}
    }
}