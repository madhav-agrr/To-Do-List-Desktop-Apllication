import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class TodoListApp {
    // Define the main frame
    private JFrame frame;

    // Define the list model and the list itself
    private DefaultListModel<Task> todoListModel;
    private JList<Task> todoList;

    // Define input elements
    private JTextField taskTextField;
    private JComboBox<String> priorityComboBox;
    private JButton addButton;
    private JButton removeButton;
    private JCheckBox completedCheckBox;

    public TodoListApp() {
        // Create the main frame
        frame = new JFrame("To-Do List App");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Create the to-do list model and JList
        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        todoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set a custom cell renderer for task display
        todoList.setCellRenderer(new TaskRenderer());

        // Create a scroll pane for the to-do list
        JScrollPane scrollPane = new JScrollPane(todoList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for input elements
        JPanel inputPanel = new JPanel(new BorderLayout());

        // Create a panel for the text field and "Add" button
        JPanel textAddPanel = new JPanel(new BorderLayout());

        // Create the task text field
        taskTextField = new JTextField();
        textAddPanel.add(taskTextField, BorderLayout.CENTER);

        // Create the "Add" button
        addButton = new JButton("Add");
        addButton.addActionListener(e -> addTask());
        textAddPanel.add(addButton, BorderLayout.EAST);

        // Create the priority combo box
        String[] priorities = {"High", "Medium", "Low"};
        priorityComboBox = new JComboBox<>(priorities);
        inputPanel.add(priorityComboBox, BorderLayout.WEST);
        inputPanel.add(textAddPanel, BorderLayout.CENTER);

        // Create the "Remove" button
        removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> removeTask());
        inputPanel.add(removeButton, BorderLayout.EAST);

        // Create the "Mark Completed" checkbox
        completedCheckBox = new JCheckBox("Mark Completed");
        completedCheckBox.addActionListener(e -> markTaskCompleted());
        inputPanel.add(completedCheckBox, BorderLayout.NORTH);

        // Add a ListSelectionListener to the todoList
        todoList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Check if the next element is selected and uncheck the "Mark Completed" checkbox
                    int selectedIndex = todoList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < todoListModel.getSize() - 1) {
                        completedCheckBox.setSelected(false);
                    }
                }
            }
        });

        // Add an ActionListener to the task text field to handle Enter key
        taskTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        // Add the input panel to the frame
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to add a task to the list
    private void addTask() {
        String taskText = taskTextField.getText().trim();
        if (!taskText.isEmpty()) {
            String priority = (String) priorityComboBox.getSelectedItem();
            Task task = new Task(taskText, priority);
            todoListModel.addElement(task);
            taskTextField.setText("");
        } else {
            // Show an error message if the task text is empty
            JOptionPane.showMessageDialog(frame, "Task cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove a selected task from the list
    private void removeTask() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            todoListModel.removeElementAt(selectedIndex);
        } else {
            // Show an error message if no task is selected for removal
            JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to mark a selected task as completed
    private void markTaskCompleted() {
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            Task selectedTask = todoListModel.getElementAt(selectedIndex);
            selectedTask.setCompleted(completedCheckBox.isSelected());
            
            // Repaint the list to reflect changes
            todoList.repaint();
        }else {
            // Handle no task selected (error handling)
            JOptionPane.showMessageDialog(frame, "No task selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner class representing a task
    private class Task {
        private String text;
        private String priority;
        private boolean completed;

        public Task(String text, String priority) {
            this.text = text;
            this.priority = priority;
            this.completed = false;
        }

        public String getText() {
            return text;
        }

        public String getPriority() {
            return priority;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        @Override
        public String toString() {
            return text + " (" + priority + ")";
        }
    }

    // Inner class for rendering tasks in the list
    private class TaskRenderer extends JPanel implements ListCellRenderer<Task> {
        private JLabel label;
        private Task task;

        public TaskRenderer() {
            setLayout(new BorderLayout());
            label = new JLabel();
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
            this.task = task;
            label.setText(task.toString());

            if (isSelected) {
                if (completedCheckBox.isSelected() && task.isCompleted()) {
                    setBackground(Color.GREEN.brighter());
                } else {
                    setBackground(list.getSelectionBackground());
                }
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (task.isCompleted()) {
                label.setForeground(Color.GREEN.darker());
            } else {
                label.setForeground(Color.BLACK);
            }

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoListApp());
    }
}
