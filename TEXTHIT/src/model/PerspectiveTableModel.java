package model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import view.PerspectiveEditorWindow;

/**
 * Table model class for the PerspectiveEditorWindow
 * ({@link PerspectiveEditorWindow})
 */
public class PerspectiveTableModel extends AbstractTableModel {

    private String[] columnNames = null;
    private ArrayList<ArrayList<Object>> rowData = null;
    private boolean readOnly = false;

    /**
     * Constructs the perspective editor table model.
     *
     * @param columnNames an array with the column names for the table
     * @param tableData an array containing each row data
     */
    public PerspectiveTableModel(String[] columnNames, Object[][] tableData) {
        ArrayList<Object> columnData = null;

        this.readOnly = false;
        this.columnNames = columnNames;

        rowData = new ArrayList<ArrayList<Object>>();
        try {
            for (int rows = 0; rows < tableData.length; rows++) {
                // create a column
                columnData = new ArrayList<Object>();
                for (int columns = 0; columns < tableData[rows].length; columns++) {
                    columnData.add(tableData[rows][columns]);
                }
                // add row with columns
                rowData.add(columnData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs the perspective editor table model.
     *
     * @param columnNames an array with the column names for the table
     * @param tableData an array containing each row data
     * @param readOnly flag to indicate that this table mode does not allow
     * edits
     */
    public PerspectiveTableModel(String[] columnNames, Object[][] tableData, boolean readOnly) {
        this(columnNames, tableData);
        this.readOnly = readOnly;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return rowData.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return ((ArrayList) rowData.get(row)).get(col);
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if ((col < 0) || readOnly) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        ((ArrayList<Object>) rowData.get(row)).set(col, value);

        fireTableCellUpdated(row, col);
    }

    public void appendRow(Object[] newRowData) {
        ArrayList<Object> columnData = null;
        columnData = new ArrayList<Object>();

        columnData.add(newRowData[0]);
        columnData.add(newRowData[1]);

        // add row with columns
        rowData.add(columnData);

        this.fireTableRowsInserted(rowData.size() - 1, rowData.size());
    }

    public void removeRow(int row) {
        rowData.remove(row);

        fireTableDataChanged();
    }
}
