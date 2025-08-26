## 📦 To be added
### Features:
- CublexCore logging support
- CublexCore localization support
- Command to reload server links

### Pre-set Dialog Screens:
- Server links
- Terms of Usage and Privacy Policy screen upon first join
- Patch Notes directory, status for each player
- Example teleport menu
- Custom favourite commands screen for players
- Basic confirmation screen template to be used in other dialogs
- Basic confirmation screen template to be used in events (plain text, yes -> teleport, no -> do nothing)
- Give Feedback / Report Bugs
- Register / Login screen


1- Every dialog must have "type". Supported types: "confirmation", "multi_action" and "notice"
2- Every dialog type can have bodies section which includes "plain_message" and/or "item" entries
3- Every dialog type can have "can_close_with_escape" option
4- Every dialog type must have "title" and "external_title"
5- Every dialog type can have "inputs" section: "text", "boolean", "single_option" and "number_range"
6- As extra, "notice" dialog type must have only 1 button and it's called "exit_button"
7- As extra, "confirmation" dialog type must have 2 exit buttons and they are called "yes" and "no"
8- As extra, "multi_action" dialog type can have 1 "exit_button" and can have many regular action buttons. And it has a key option "columns"

9- Buttons! This is another thing. This is "ActionButton". Every button has the same build. "action()", "label()", "tooltip()" and "width()"

10- ActionButton actions are "show_dialog", "open_url", "run_command", "suggest_command", "copy_to_clipboard" and "custom". "custom" is "type: custom" "id: any identify to event" "payload: optional payload of the event"

11- Every "input" has a "key" ("id" in configuration YML for now) to be used in "events"
12- Every "input" has "label"
13- Extra, "text" input type has "width", "label_visible", "initial", "max_length", "multiline.max_lines" and "multiline.height" options
14- Extra, "boolean" input type has "initial", "on_true" and "on_false" options
15- Extra, "single_option" input type has "label_visible", "width", "options.option.id", "options.option.display", "options.option.initial". "options.option.initial" is boolean
16- Extra, "number_range" input type has "label_format", "width", "start", "end", "step" and "initial" options
