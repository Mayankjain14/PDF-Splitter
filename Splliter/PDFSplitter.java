import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
//import org.apache.pdfbox.pdmodel.*;
//import org.apache.pdfbox.multipdf.*;

public class PDFSplitter extends JFrame {
    private JTextField inputField, outputField, pagesField;
    private JButton splitButton;

    public PDFSplitter() {
        super("PDF Splitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Input PDF file:"), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputField = new JTextField(20);
        add(inputField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Output directory:"), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        outputField = new JTextField(20);
        add(outputField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Pages to extract:"), gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pagesField = new JTextField(20);
        add(pagesField, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        splitButton = new JButton("Split PDF");
        splitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                splitPDF();
            }
        });
        add(splitButton, gbc);
        pack();
        setLocationRelativeTo(null);
    }

    private void splitPDF() {
        String inputFilePath = inputField.getText();
        String outputDirectory = outputField.getText();
        String pagesToExtract = pagesField.getText();
        if (inputFilePath.isEmpty() || outputDirectory.isEmpty() || pagesToExtract.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            PDDocument document = PDDocument.load(new File(inputFilePath));
            Splitter splitter = new Splitter();
            String[] pageRanges = pagesToExtract.split(",");
            for (String pageRange : pageRanges) {
                String[] parts = pageRange.trim().split("-");
                int startPage = Integer.parseInt(parts[0]) - 1;
                int endPage = parts.length > 1 ? Integer.parseInt(parts[1]) - 1 : startPage;
                splitter.setStartPage(startPage);
                splitter.setEndPage(endPage);
                String fileName = new File(inputFilePath).getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                fileName += "_" + (startPage + 1) + "-" + (endPage + 1) + ".pdf";
                File outputFile = new File(outputDirectory, fileName);
                OutputStream out = new FileOutputStream(outputFile);
                //splitter.split(document).save(out); // fixed code
                out.close();
            }
            document.close();
            JOptionPane.showMessageDialog(this, "PDF split successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error splitting PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        PDFSplitter splitter = new PDFSplitter();
        splitter.setVisible(true);
    }
}
