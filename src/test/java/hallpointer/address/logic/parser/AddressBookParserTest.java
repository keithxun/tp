package hallpointer.address.logic.parser;

import static hallpointer.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static hallpointer.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static hallpointer.address.testutil.Assert.assertThrows;
import static hallpointer.address.testutil.TypicalIndexes.INDEX_FIRST_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import hallpointer.address.logic.commands.AddMemberCommand;
import hallpointer.address.logic.commands.ClearCommand;
import hallpointer.address.logic.commands.DeleteMemberCommand;
import hallpointer.address.logic.commands.ExitCommand;
import hallpointer.address.logic.commands.FindMemberCommand;
import hallpointer.address.logic.commands.HelpCommand;
import hallpointer.address.logic.commands.ListMembersCommand;
import hallpointer.address.logic.commands.UpdateMemberCommand;
import hallpointer.address.logic.commands.UpdateMemberCommand.UpdateMemberDescriptor;
import hallpointer.address.logic.parser.exceptions.ParseException;
import hallpointer.address.model.member.Member;
import hallpointer.address.model.member.NameContainsKeywordsPredicate;
import hallpointer.address.testutil.MemberBuilder;
import hallpointer.address.testutil.MemberUtil;
import hallpointer.address.testutil.UpdateMemberDescriptorBuilder;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_addMember() throws Exception {
        Member member = new MemberBuilder().build();
        AddMemberCommand command = (AddMemberCommand) parser.parseCommand(MemberUtil.getAddCommand(member));
        assertEquals(new AddMemberCommand(member), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_deleteMember() throws Exception {
        DeleteMemberCommand command = (DeleteMemberCommand) parser.parseCommand(
                DeleteMemberCommand.COMMAND_WORD + " " + INDEX_FIRST_MEMBER.getOneBased());
        assertEquals(new DeleteMemberCommand(INDEX_FIRST_MEMBER), command);
    }

    @Test
    public void parseCommand_updateMember() throws Exception {
        Member member = new MemberBuilder().build();
        UpdateMemberDescriptor descriptor = new UpdateMemberDescriptorBuilder(member).build();
        UpdateMemberCommand command = (UpdateMemberCommand) parser.parseCommand(UpdateMemberCommand.COMMAND_WORD + " "
                + INDEX_FIRST_MEMBER.getOneBased() + " " + MemberUtil.getUpdateMemberDescriptorDetails(descriptor));
        assertEquals(new UpdateMemberCommand(INDEX_FIRST_MEMBER, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_findMember() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindMemberCommand command = (FindMemberCommand) parser.parseCommand(
                FindMemberCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindMemberCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_listMembers() throws Exception {
        assertTrue(parser.parseCommand(ListMembersCommand.COMMAND_WORD) instanceof ListMembersCommand);
        assertTrue(parser.parseCommand(ListMembersCommand.COMMAND_WORD + " 3") instanceof ListMembersCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
