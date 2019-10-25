package lpp;

import logging.Logger;
import numbers.Value;
import print.ColorPrint;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LPPReader {


    public LPP readFromFile(File file) {
        Logger.println("info","Reading data from file...");

        byte[] data = new byte[0];
        try {
            FileInputStream fis = new FileInputStream(file);
            data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
        }
        catch (FileNotFoundException fnf){
            fnf.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String all = new String(data, StandardCharsets.UTF_8)
                .replace(" ","")
                .replaceAll("#.*","")
                .replaceAll("\\n\\r","")
                .toLowerCase()
                + "\n ";

        //System.out.println("================================================\n" + all + "\n================================================");
        Scanner scanner = new Scanner(all).useLocale(Locale.US);
        String line = scanner.nextLine();


        // Find problem type
        String patternStr = "(.*)\\(";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        ProblemType problemType = ProblemType.fromString(matcher.group(1));

        // Find objective function
        pattern = Pattern.compile("\\((.*)\\)");
        matcher = pattern.matcher(line);
        matcher.find();
        String[] strValues = matcher.group(1).split(",");
        int numVar = strValues.length;
        ArrayList<Component> obj = new ArrayList<>();
        for(int i =0; i< numVar; i++){
            Component comp = new Component("xxx",i,new Value(Double.parseDouble(strValues[i])));
            obj.add(comp);
        }
        // Find solution vector symbol
        pattern = Pattern.compile("\\)(.*)$");
        matcher = pattern.matcher(line);
        matcher.find();
        String solution = matcher.group(1);
        for(Component comp : obj){
            comp.setGroup(solution);
        }

        line = scanner.nextLine();
        if(line.contains("s.t")) line = scanner.nextLine();
        boolean done = false;
        // Find restriction on A matrix
        ArrayList<Restriction> rts = new ArrayList<>();
        while(!done && scanner.hasNextLine()){
            pattern = Pattern.compile("^\\(([^=<>]*)\\)y([=<>]*)([0-9.]*)");
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                strValues = matcher.group(1).split(",");
                Value[] leftSide = new Value[strValues.length];
                for (int i = 0; i < strValues.length; i++) {
                    leftSide[i] = new Value(Double.parseDouble(strValues[i]));
                }
                RestrictionType rtt = RestrictionType.fromString(matcher.group(2));
                Value rightSide = new Value(Double.parseDouble(matcher.group(3)));
                Restriction rt = new Restriction(leftSide,rightSide,rtt,solution);
                rts.add(rt);
                line = scanner.nextLine();
            }
            else{
                done = true;
            }
        }

        ArrayList<VariableRestriction> vrts = new ArrayList<>();

        // Find variable-specific restrictions on intervals
        done = false;
        while(!done && scanner.hasNextLine()){
            patternStr = "^("+solution+"[^=<>:]*)([=<>]+)([0-9.]+)";
            pattern = Pattern.compile(patternStr);
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                strValues = matcher.group(1).split(",");
                for(String str : strValues){
                    RestrictionType rtt = RestrictionType.fromString(matcher.group(2));
                    Value rightSide = new Value(Double.parseDouble(matcher.group(3)));
                    VariableRestriction vrt = new VariableRestriction(str,rightSide,rtt);
                    vrts.add(vrt);
                }
                line = scanner.nextLine();
            }
            else{
                done = true;
            }
        }
        // Find variable-specific restrictions on domains
        done = false;
        while(!done && scanner.hasNextLine()){
            patternStr = "^("+solution+"[^=<>:]*)([:]+[a-z]+)";
            pattern = Pattern.compile(patternStr);
            matcher = pattern.matcher(line);
            if (matcher.find()) {
                strValues = matcher.group(1).split(",");
                for(String str : strValues){
                    RestrictionType rtt = RestrictionType.fromString(matcher.group(2));
                    VariableRestriction vrt = new VariableRestriction(str,new Value(0),rtt);
                    vrts.add(vrt);
                }
                line = scanner.nextLine();
            }
            else{
                done = true;
            }
        }

        return new LPP(problemType,rts,vrts,obj,solution);
    }


    private ProblemType findProblemType(String rttStr) {
        ProblemType problemType = null;
        if(rttStr.contains("max")){
            problemType = ProblemType.MAX;
        }
        else if(rttStr.contains("min")){
            problemType = ProblemType.MIN;
        }
        else{
            System.out.println("ProblemType '" + rttStr + "' is not known.");
            System.exit(1);
        }
        return problemType;
    }

}
