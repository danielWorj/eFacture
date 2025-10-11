package com.server.eFacture.Utils;



import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.server.eFacture.DTO.JSON.TacheMontantDTO;
import com.server.eFacture.Entity.Devis.Devis;
import com.server.eFacture.Entity.Devis.Enregistrement;
import com.server.eFacture.Entity.Entreprise.Tache;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class GeneratePDF {
    public void generateDevis(Devis devis, Tache tache ,  List<Enregistrement> enregistrementList) throws FileNotFoundException, MalformedURLException {
        String namePdfFile = devis.getClient().getNom()+" "+tache.getIntitule()+".pdf"; //Nom du pdf
        PdfWriter pdfWriter = new PdfWriter(namePdfFile);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        String imagePath = "D:\\Projet Facture iTextPDF\\server\\src\\main\\resources\\static\\logobusiness.jpg";

        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);


        /*float x= pdfDocument.getDefaultPageSize().getWidth()/2;
        float y= pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setOpacity(0.1f);
        document.add(image);**/

        //Les deux colones d'entete;
        float threecol=190f;
        float twocol = 285f;//taille de la premiere colonne
        float twocol150 = twocol + 150f ;
        float twocolumnWidth[] = {twocol150,twocol};
        float threeColumnWidth[] = {threecol,threecol,threecol};
        float fourColumnWidth[] = {threecol,threecol,threecol,threecol};
        float fullwidth[]={threecol*3 };
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twocolumnWidth);
        table.addCell(new Cell().add("Devis").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());

        Table nestedTable = new Table(new float[]{twocol/2,twocol/2});
        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(devis.getId())));
        nestedTable.addCell(getHeaderTextCell("Invoice Date."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(new Date())));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Border gb=new SolidBorder(Color.GRAY,1f/2f);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);

        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);

        Table twoColTable = new Table(twocolumnWidth);
        twoColTable.addCell(getBillingandShippingCell("Prestataire"));
        twoColTable.addCell(getBillingandShippingCell("Maitre d'ouvrage "));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Nom :",true));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getTechnicien().getNom()),true));
        twoColTable2.addCell(getCell10fLeft("Contact",false));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getTechnicien().getTelephone()),false));
        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Company",true));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getClient().getNom()),true));
        twoColTable2.addCell(getCell10fLeft("Contact :",false));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getClient().getTelephone()),false));
        document.add(twoColTable2);

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(Color.GRAY,0.5f);
        document.add(tableDivider2.setBorder(dgb));

        Paragraph productParagraph = new Paragraph(String.valueOf(tache.getIntitule()));
        document.add(productParagraph.setBold());

        Table threeColTable1 = new Table(4);
        threeColTable1.setBackgroundColor(Color.BLACK,0.7f);

        threeColTable1.addCell(new Cell().add("Intitule").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add("Quantite").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add("Prix Unitaire").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add("Price").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        document.add(threeColTable1);


        Table threeColTable2 = new Table(4);

        float totalSum = 0;
        for (int i = 0; i < enregistrementList.size(); i++) {
            float total = enregistrementList.get(i).getQuantite() * enregistrementList.get(i).getMateriel().getPrixUnitaire();
            totalSum =totalSum+total;
            threeColTable2.addCell(new Cell().add(enregistrementList.get(i).getMateriel().getIntitule())).setMarginLeft(10f);
            threeColTable2.addCell(new Cell().add(String.valueOf(enregistrementList.get(i).getQuantite())).setTextAlignment(TextAlignment.CENTER));
            threeColTable2.addCell(new Cell().add(String.valueOf(enregistrementList.get(i).getMateriel().getPrixUnitaire())).setTextAlignment(TextAlignment.CENTER));
            threeColTable2.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
        }

        document.add(threeColTable2.setMarginBottom(20f));
        /*float onetwo[]={threecol=125f, threecol*2};
        Table threeColTable4 = new Table(onetwo);
        threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
        threeColTable4.addCell(new Cell().add(tableDivider2)).setBorder(Border.NO_BORDER);
        document.add(threeColTable4);*/
        document.add(tableDivider2);


        Table threeColTable3 = new Table(threeColumnWidth);
        threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
        threeColTable3.addCell(new Cell().add("Total").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginLeft(15f);
        document.add(threeColTable3);

        document.add(tableDivider2);
        document.add(new Paragraph("\n"));
        document.add(divider.setBorder(new SolidBorder(Color.GRAY,1)).setMarginBottom(15f));
        Table tb = new Table(fullwidth);
        tb.addCell(new Cell().add("TERMS AND CONDITIONS\n").setBold().setBorder(Border.NO_BORDER));

        List<String> TncList = new ArrayList<>();
        TncList.add("1- The seller shall not be liable to the buyer ...");
        TncList.add("2- The seller warrants the product for one (1) year from the date of shipment");

        for (String tnc:TncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }

        document.add(tb);


        document.close();

        fileCopy(devis.getClient().getNom(),namePdfFile);

        deleteFileInSource(namePdfFile);
    }
    public void generateRecapitulatif(Devis devis,  List<TacheMontantDTO> tacheMontantDTOList) throws FileNotFoundException, MalformedURLException {
        String namePdfFile = devis.getClient().getNom()+" Recapitulatif.pdf"; //Nom du pdf
        PdfWriter pdfWriter = new PdfWriter(namePdfFile);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        String imagePath = "D:\\Projet Facture iTextPDF\\server\\src\\main\\resources\\static\\logobusiness.jpg";

        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);


        /*float x= pdfDocument.getDefaultPageSize().getWidth()/2;
        float y= pdfDocument.getDefaultPageSize().getHeight()/2;
        image.setOpacity(0.1f);
        document.add(image);**/

        //Les deux colones d'entete;
        float threecol=190f;
        float twocol = 285f;//taille de la premiere colonne
        float twocol150 = twocol + 150f ;
        float twocolumnWidth[] = {twocol150,twocol};
        float threeColumnWidth[] = {threecol,threecol,threecol};
        float fourColumnWidth[] = {threecol,threecol,threecol,threecol};
        float fullwidth[]={threecol*3 };
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twocolumnWidth);
        table.addCell(new Cell().add("Devis").setFontSize(20f).setBorder(Border.NO_BORDER).setBold());

        Table nestedTable = new Table(new float[]{twocol/2,twocol/2});
        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(devis.getId())));
        nestedTable.addCell(getHeaderTextCell("Invoice Date."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(new Date())));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Border gb=new SolidBorder(Color.GRAY,1f/2f);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);

        document.add(table);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);

        Table twoColTable = new Table(twocolumnWidth);
        twoColTable.addCell(getBillingandShippingCell("Prestataire"));
        twoColTable.addCell(getBillingandShippingCell("Maitre d'ouvrage "));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Nom :",true));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getTechnicien().getNom()),true));
        twoColTable2.addCell(getCell10fLeft("Contact",false));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getTechnicien().getTelephone()),false));
        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Company",true));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getClient().getNom()),true));
        twoColTable2.addCell(getCell10fLeft("Contact :",false));
        twoColTable2.addCell(getCell10fLeft(String.valueOf(devis.getClient().getTelephone()),false));
        document.add(twoColTable2);

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(Color.GRAY,0.5f);
        document.add(tableDivider2.setBorder(dgb));

        Table threeColTable1 = new Table(2);
        threeColTable1.setBackgroundColor(Color.BLACK,0.7f);

        threeColTable1.addCell(new Cell().add("Intitule").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER)).setMarginLeft(10f);
        threeColTable1.addCell(new Cell().add("Price").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        document.add(threeColTable1);


        Table threeColTable2 = new Table(2);

        float totalSum = 0;
        for (int i = 0; i < tacheMontantDTOList.size(); i++) {
            float total = tacheMontantDTOList.get(i).getMontant(); //montant par tache
            totalSum =totalSum+total;
            threeColTable2.addCell(new Cell().add(tacheMontantDTOList.get(i).getTache().getIntitule())).setMarginLeft(10f);
            threeColTable2.addCell(new Cell().add(String.valueOf(tacheMontantDTOList.get(i).getMontant()) +" Fcfa").setTextAlignment(TextAlignment.RIGHT).setMarginRight(15f));
        }

        document.add(threeColTable2.setMarginBottom(20f));
        document.add(tableDivider2);


        Table threeColTable3 = new Table(threeColumnWidth);
        threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setMarginLeft(10f));
        threeColTable3.addCell(new Cell().add("Total").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)+" Fcfa").setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setFontColor(Color.RED).setMarginLeft(15f);
        document.add(threeColTable3);

        document.add(tableDivider2);
        document.add(new Paragraph("\n"));
        document.add(divider.setBorder(new SolidBorder(Color.GRAY,1)).setMarginBottom(15f));
        Table tb = new Table(fullwidth);
        tb.addCell(new Cell().add("TERMS AND CONDITIONS\n").setBold().setBorder(Border.NO_BORDER));

        List<String> TncList = new ArrayList<>();
        TncList.add("1- The seller shall not be liable to the buyer ...");
        TncList.add("2- The seller warrants the product for one (1) year from the date of shipment");

        for (String tnc:TncList){
            tb.addCell(new Cell().add(tnc).setBorder(Border.NO_BORDER));
        }

        document.add(tb);


        document.close();

        fileCopy(devis.getClient().getNom(),namePdfFile);

        deleteFileInSource(namePdfFile);
    }

    public static void fileCopy(String nomClient , String nomPdf){
       // Path sourcePath = Paths.get("./Source/img.png");
       // Path destinatePath = Paths.get("./Destination/img.png");
        //String nomDossier = devis.getClient().getNom();
        Path source = Paths.get("./"+nomPdf); //Source du pdf
        Path dossier = Paths.get("./"+nomClient);
        Path destination = Paths.get("./"+nomClient+"/"+nomPdf);
        try {
            //Creation du dossier
            //Check si le dossier existe deja
            if (!Files.isDirectory(dossier)){
                Path dir = Files.createDirectory(Path.of("./"+nomClient)); //creation
            }
            //On cree un dossier
            Files.copy(source,destination);
            //System.out.println("File copied successfully");
        }catch (
                IOException e){
            System.err.println("Error copying file: " + e.getMessage());
        }

    }

    public static void deleteFileInSource(String fileNamePdf){
        Path source = Paths.get("./"+fileNamePdf);
        try {
            if (Files.exists(source)){

                Files.delete(source); //Si le fichier existe on supprime d'ou il provient
                System.out.println("Fichier "+fileNamePdf+" Supprime dans la racine");
            }else{
                //sinon on envoie en console qu'il n'existe pas
                System.out.println("Le fichier n'existe pas a la source");
            }
        }catch (Exception e){
            System.err.println("Erreur de suppression du  fichier :"+e.getMessage());
        }
    }
    public static Cell getHeaderTextCell(String textValue){
        return new Cell()
                .add(textValue)
                .setBold()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }
    public static Cell getHeaderTextCellValue(String textValue){
        return new Cell()
                .add(textValue)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }
    public static Cell getBillingandShippingCell(String textValue){
        return new Cell()
                .add(textValue)
                .setFontSize(12f)
                .setBold()
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.LEFT);
    }
    public static Cell getCell10fLeft(String textvalue, Boolean isBold){
        Cell mycell = new Cell()
                    .add(textvalue)
                    .setFontSize(10f)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT);
        return isBold ? mycell.setBold():mycell;
    }
}
