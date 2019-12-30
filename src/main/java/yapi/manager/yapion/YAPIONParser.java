package yapi.manager.yapion;

import yapi.exceptions.YAPIException;
import yapi.exceptions.objectnotation.YAPIONException;
import yapi.manager.json.JSONVariable;
import yapi.manager.json.value.JSONArray;
import yapi.manager.json.value.JSONObject;
import yapi.manager.json.value.JSONValue;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.util.List;

public class YAPIONParser {

    public static void main(String[] args) {
        System.out.println(YAPIONParser.parse("{Test(Hello World)    Test2{Hallo(10)Boolean(true)Boolean2(false)} Papa[Hallo,{Test(Hallo)}]Help{}}"));
        System.out.println(YAPIONParser.toJSON(YAPIONParser.parse("{Test(Hello World)    Test2{Hallo(10)Boolean(true)Boolean2(false)} Papa[Hallo,{Test(Hallo)}]Help{}}")).toString());
    }

    public static JSONObject toJSON(YAPIONObject yapionObject) {
        JSONObject jsonObject = new JSONObject();
        List<String> keys = yapionObject.getKeys();
        for (String s : keys) {
            YAPIONType yapionType = yapionObject.getValue(s);
            if (yapionType instanceof YAPIONValue) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONValue) yapionType)));
            } else if (yapionType instanceof YAPIONArray) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONArray) yapionType)));
            } else if (yapionType instanceof YAPIONObject) {
                jsonObject.add(new JSONVariable(s, toJSON((YAPIONObject) yapionType)));
            }
        }
        return jsonObject;
    }

    private static JSONArray toJSON(YAPIONArray yapionArray) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < yapionArray.size(); i++) {
            YAPIONType yapionType = yapionArray.get(i);
            if (yapionType instanceof YAPIONValue) {
                jsonArray.add(toJSON((YAPIONValue) yapionType));
            } else if (yapionType instanceof YAPIONArray) {
                jsonArray.add(toJSON((YAPIONArray) yapionType));
            } else if (yapionType instanceof YAPIONObject) {
                jsonArray.add(toJSON((YAPIONObject) yapionType));
            }
        }
        return jsonArray;
    }

    private static JSONValue toJSON(YAPIONValue yapionValue) {
        return new JSONValue(yapionValue.getValueAsJSON());
    }

    /**
     * A new YAPION object starts with '{' and end with '}'.
     * A key starts with any character stat is a non whitespace character and ends with '(', '[' or '{'.
     * The normal bracket indicates a value like a string, character, number or boolean.
     * The square bracket indicates a array out of values.
     * The curly bracket indicates a object out of key-value pares.
     *
     * Example
     *   {}
     *   {Test(Hello World)}
     *   {Test[10, 9, 8]}
     *   {Test{Test(Hello World)}}
     *
     * @since Version 1.1
     *
     * @param yapion
     * @return
     */
    public static YAPIONObject parse(String yapion) {
        char[] chars = yapion.toCharArray();
        if (chars[0] != '{' || chars[chars.length - 1] != '}') {
            throw new YAPIONException("No object input\n" + createErrorMessage(chars, 0, chars.length));
        }
        try {
            return parse(yapion.substring(1, yapion.length() - 1).toCharArray());
        } catch (YAPIONException e) {
            throw new YAPIException(e.getMessage());
        }
    }

    private static YAPIONObject parse(char[] chars) {
        YAPIONObject yapionObject = new YAPIONObject();

        boolean escaped = false;
        StringBuilder key = new StringBuilder();
        int i = 0;
        while (i < chars.length) {
            if (chars[i] == '\\') {
                escaped = true;
                i++;
                continue;
            }

            if (!escaped && chars[i] == '(') {
                String s = getValue(chars, i);
                i += s.length() + 1;
                yapionObject.add(new YAPIONVariable(key.toString(), new YAPIONValue(s)));
                key = new StringBuilder();
            } else if (!escaped && chars[i] == '[') {
                String s = getValue(chars, i);
                yapionObject.add(new YAPIONVariable(key.toString(), parseArray(chars, i + 1, i + s.length() + 1)));
                i += s.length() + 1;
                key = new StringBuilder();
            } else if (!escaped && chars[i] == '{') {
                String s = getValue(chars, i);
                i += s.length() + 1;
                yapionObject.add(new YAPIONVariable(key.toString(), parse(s.toCharArray())));
                key = new StringBuilder();
            } else if ((chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b') && key.length() > 0) {
                key.append(chars[i]);
            } else if (!(chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b')) {
                key.append(chars[i]);
            }
            escaped = false;
            i++;
        }

        return yapionObject;
    }

    private static String createErrorMessage(char[] chars, int index, int indexEnd) {
        index--;
        indexEnd--;
        while (chars[indexEnd] == ' ' || chars[indexEnd] == '\t' || chars[indexEnd] == '\n' || chars[indexEnd] == '\b') {
            indexEnd--;
        }
        StringBuilder message = new StringBuilder();
        StringBuilder error   = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '\n') {
                message.append("\\n");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else if (chars[i] == '\t') {
                message.append("\\t");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else if (chars[i] == '\b') {
                message.append("\\b");
                if (i == index) {
                    error.append('^').append('~');
                } else if (i > index && i <= indexEnd) {
                    error.append('~').append('~');
                } else {
                    error.append(' ').append(' ');
                }
            } else {
                message.append(chars[i]);
                if (i == index) {
                    error.append('^');
                } else if (i > index && i <= indexEnd) {
                    error.append('~');
                } else {
                    error.append(' ');
                }
            }
        }
        return message + "\n" + error;
    }

    private static String getValue(char[] chars, int index) {
        StringBuilder st = new StringBuilder();
        int bracket = 1;
        int type;
        if (chars[index] == '(') {
            type = 0;
        } else if (chars[index] == '[') {
            type = 1;
        } else if (chars[index] == '{') {
            type = 2;
        } else if (chars[index] == '<') {
            type = 3;
        } else {
            throw new YAPIONException("Unknown starting bracket at " + index + "\n" + createErrorMessage(chars, index, index));
        }
        index++;
        for (int i = index; i < chars.length; i++) {
            if (type == 0) {
                if (chars[i] == '(') {
                    bracket++;
                }
                if (chars[i] == ')') {
                    bracket--;
                }
            } else if (type == 1) {
                if (chars[i] == '[') {
                    bracket++;
                }
                if (chars[i] == ']') {
                    bracket--;
                }
            } else if (type == 2) {
                if (chars[i] == '{') {
                    bracket++;
                }
                if (chars[i] == '}') {
                    bracket--;
                }
            } else {
                if (chars[i] == '<') {
                    bracket++;
                }
                if (chars[i] == '>') {
                    bracket--;
                }
            }
            if (bracket == 0) {
                return st.toString();
            } else {
                st.append(chars[i]);
            }
        }
        throw new YAPIONException("Missing closing bracket for opening bracket at " + index + "\n" + createErrorMessage(chars, index, chars.length));
    }

    private static YAPIONArray parseArray(char[] chars, int start, int end) {
        YAPIONArray yapionArray = new YAPIONArray();
        boolean escaped = false;
        StringBuilder st = new StringBuilder();
        for (int i = start; i < end; i++) {
            if (chars[i] == '\\') {
                escaped = true;
                continue;
            }
            if (!escaped && chars[i] == ',') {
                String s = st.toString();
                st = new StringBuilder();
                yapionArray.add(parseArray(s));
            } else if ((chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b') && st.length() > 0) {
                st.append(chars[i]);
            } else if (!(chars[i] == ' ' || chars[i] == '\t' || chars[i] == '\n' || chars[i] == '\b')) {
                st.append(chars[i]);
            }
            escaped = false;
        }
        if (st.length() != 0) {
            yapionArray.add(parseArray(st.toString()));
        }
        return yapionArray;
    }

    private static YAPIONType parseArray(String s) {
        YAPIONType yapionType;
        if (s.length() == 0) {
            return null;
        }
        char[] chars = s.substring(1, s.length() - 1).toCharArray();
        if (s.startsWith("[") && s.endsWith("]")) {
            yapionType = parseArray(chars, 0, chars.length);
        } else if (s.startsWith("{") && s.endsWith("}")) {
            yapionType = parse(chars);
        } else {
            yapionType = new YAPIONValue(s);
        }
        return yapionType;
    }

}
