package yapi.manager.yapion;

import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;

public class YAPIONVariable {

    private String name = "";
    private YAPIONType yapionType = null;

    public YAPIONVariable(String name, YAPIONType yapionType) {
        this.name = name;
        this.yapionType = yapionType;
    }

    public String getName() {
        return name;
    }

    public YAPIONType getYapionType() {
        return yapionType;
    }

    @Override
    public String toString() {
        return name.replaceAll("([({\\[)}\\]\\\\ ])", "\\\\$1") + yapionType.toString();
    }

    public String toHierarchyString(int index) {
        if (yapionType instanceof YAPIONArray) {
            return " ".repeat(2 * index) + name.replaceAll("([({\\[)}\\]\\\\ ])", "\\\\$1") + ((YAPIONArray) yapionType).toHierarchyString(index + 1);
        } else if (yapionType instanceof YAPIONObject) {
            return " ".repeat(2 * index) + name.replaceAll("([({\\[)}\\]\\\\ ])", "\\\\$1") + ((YAPIONObject) yapionType).toHierarchyString(index + 1);
        } else {
            return " ".repeat(2 * index) + name.replaceAll("([({\\[)}\\]\\\\ ])", "\\\\$1") + yapionType.toString();
        }
    }

}