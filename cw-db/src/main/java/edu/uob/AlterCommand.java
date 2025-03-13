package edu.uob;

public class AlterCommand extends Parser {
    @Override
    public DBCommand parse(TokenBank tokenBank) {
        tokenBank.nextToken();
        if (tokenBank.nextToken().equals("ADD")) {
            DBCommand addAttribute = new AddAttribute();
            return addAttribute.parse(tokenBank);
        }
        else if (tokenBank.nextToken().equals("DROP")) {
            DBCommand dropAttribute = new DropAttribute();
            return dropAttribute.parse(tokenBank);
        }
        return null;
    }
}
