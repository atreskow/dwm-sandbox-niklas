using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using PdfSharp;
using MigraDoc.DocumentObjectModel;
using MigraDoc.DocumentObjectModel.Shapes;
using MigraDoc.DocumentObjectModel.Tables;
using System.Xml.XPath;
using WineExhibitionQuestionnaire.DataProvider;
using System.IO;
using System.Drawing.Imaging;
using System.Reflection;

namespace WineExhibitionQuestionnaire.Infrastructure
{

    public class InvoicePdfGenerator
    {
        private Document document;
        private TextFrame addressFrame;
        private Table table;
        private Paragraph header;

        public InvoicePdfGenerator()
        {
                        CreateDocument();
        }

        private void CreateDocument()
        {
            // Create a new MigraDoc document
            this.document = new Document();
            this.document.Info.Title = Resources.LocRes.Email_Title;
            this.document.Info.Author = "Niklas Antony";

            DefineStyles();
            CreatePage();
        }

        private void DefineStyles()
        {
            Style style = this.document.Styles["Normal"];
            style.Font.Name = "Verdana";

            style = this.document.Styles[StyleNames.Header];
            style.ParagraphFormat.AddTabStop("16cm", TabAlignment.Right);

            style = this.document.Styles[StyleNames.Footer];
            style.ParagraphFormat.AddTabStop("8cm", TabAlignment.Center);

            // Create a new style called Table based on style Normal
            style = this.document.Styles.AddStyle("Table", "Normal");
            style.Font.Name = "Verdana";
            style.Font.Name = "Times New Roman";
            style.Font.Size = 9;

            // Create a new style called Reference based on style Normal
            style = this.document.Styles.AddStyle("Reference", "Normal");
            style.ParagraphFormat.SpaceBefore = "5mm";
            style.ParagraphFormat.SpaceAfter = "5mm";
            style.ParagraphFormat.TabStops.AddTabStop("16cm", TabAlignment.Right);
        }

        private void CreatePage()
        {
            // Each MigraDoc document needs at least one section.
            Section section = this.document.AddSection();

            // Put a logo in the header
            string path = AppDomain.CurrentDomain.BaseDirectory;

            Image image = section.Headers.Primary.AddImage(path + "/Content/DWM-logo-rand.png"); 
            image.Height = "2.5cm";
            image.LockAspectRatio = true;
            image.RelativeVertical = RelativeVertical.Line;
            image.RelativeHorizontal = RelativeHorizontal.Margin;
            image.Top = ShapePosition.Top;
            image.Left = ShapePosition.Right;
            image.WrapFormat.Style = WrapStyle.Through;

            // Create footer
            Paragraph paragraph = section.Footers.Primary.AddParagraph();
            paragraph.AddText("Deutsche Wein Marketing GmbH · Am Borsigturm 1 · 13507 Berlin · Deutschland");
            paragraph.Format.Font.Size = 9;
            paragraph.Format.Alignment = ParagraphAlignment.Center;

            // Create the text frame for the address
            this.addressFrame = section.AddTextFrame();
            this.addressFrame.Height = "3.0cm";
            this.addressFrame.Width = "7.0cm";
            this.addressFrame.Left = ShapePosition.Left;
            this.addressFrame.RelativeHorizontal = RelativeHorizontal.Margin;
            this.addressFrame.Top = "5.0cm";
            this.addressFrame.RelativeVertical = RelativeVertical.Page;

            // Put sender in address frame
            paragraph = this.addressFrame.AddParagraph("Deutsche Wein Marketing GmbH · Am Borsigturm 1 · 13507 Berlin");
            paragraph.Format.Font.Name = "Times New Roman";
            paragraph.Format.Font.Size = 7;
            paragraph.Format.SpaceAfter = 3;

            // Add the print date field
            paragraph = section.AddParagraph();
            paragraph.Format.SpaceBefore = "8cm";
            paragraph.Style = "Reference";
            paragraph.AddFormattedText(Resources.LocRes.Invoice_Paragraph, TextFormat.Bold);
            paragraph.AddTab();
            paragraph.AddText("Berlin, ");
            paragraph.AddDateField("dd.MM.yyyy");

            // Create the item table
            this.table = section.AddTable();
            this.table.Style = "Table";
            this.table.Borders.Color = new Color(0,0,0);
            this.table.Borders.Width = 0.25;
            this.table.Borders.Left.Width = 0.5;
            this.table.Borders.Right.Width = 0.5;
            this.table.Rows.LeftIndent = 0;

            // Before you can add a row, you must define the columns
            Column column = this.table.AddColumn("1.5cm");
            column.Format.Alignment = ParagraphAlignment.Center;

            column = this.table.AddColumn("3.5cm");
            column.Format.Alignment = ParagraphAlignment.Right;

            column = this.table.AddColumn("3.5cm");
            column.Format.Alignment = ParagraphAlignment.Right;

            column = this.table.AddColumn("2.5cm");
            column.Format.Alignment = ParagraphAlignment.Right;

            column = this.table.AddColumn("5cm");
            column.Format.Alignment = ParagraphAlignment.Right;

            // Create the header of the table
            Row row = table.AddRow();
            row.HeadingFormat = true;
            row.Format.Alignment = ParagraphAlignment.Center;
            row.Format.Font.Bold = true;
            row.Shading.Color = new Color(200, 200, 200); ;
            row.Cells[0].AddParagraph(Resources.LocRes.Invoice_ObjectNumber);
            row.Cells[0].Format.Alignment = ParagraphAlignment.Left;
            row.Cells[0].VerticalAlignment = VerticalAlignment.Bottom;
            row.Cells[1].AddParagraph(Resources.LocRes.Invoice_Amount);
            row.Cells[1].Format.Alignment = ParagraphAlignment.Left;
            row.Cells[2].AddParagraph(Resources.LocRes.Invoice_Price_Unit);
            row.Cells[2].Format.Alignment = ParagraphAlignment.Left;
            row.Cells[3].AddParagraph(Resources.LocRes.Invoice_Shipping);
            row.Cells[3].Format.Alignment = ParagraphAlignment.Left;
            row.Cells[4].AddParagraph(Resources.LocRes.Invoice_Price);
            row.Cells[4].Format.Alignment = ParagraphAlignment.Left;
            row.Cells[4].VerticalAlignment = VerticalAlignment.Bottom;

            this.table.SetEdge(0, 0, 5, 1, Edge.Box, BorderStyle.Single, 0.75, Color.Empty);
        }


        public Document GetInvoice(Guid guid)
        {
            using (var ctx = new WineExhibitionQuestionnaireEntities())
            {
                var model = ctx.CLIENT_DATA.First(x => x.ID == guid);

                // Fill address in address text frame
                Paragraph paragraph = this.addressFrame.AddParagraph();
                paragraph.AddText(model.FIRST_NAME + " " + model.LAST_NAME);
                paragraph.AddLineBreak();

                int count = 1;
               

                // Add an invisible row as a space line to the table
                Row row5 = this.table.AddRow();
                row5.Borders.Visible = false;

                // Add the total price row
                row5 = this.table.AddRow();
                row5.Cells[0].Borders.Visible = false;
                //row5.Cells[0].AddParagraph(Resources.LocRes.Cost_Final);
                row5.Cells[0].Format.Font.Bold = true;
                row5.Cells[0].Format.Alignment = ParagraphAlignment.Right;
                row5.Cells[0].MergeRight = 3;
                this.table.SetEdge(4, this.table.Rows.Count - 1, 1, 1, Edge.Box, BorderStyle.Single, 0.75);

                // Add the notes paragraph
                paragraph = this.document.LastSection.AddParagraph();
                paragraph.Format.SpaceBefore = "1cm";
                paragraph.Format.Borders.Width = 0.75;
                paragraph.Format.Borders.Distance = 3;
               
                return this.document;
            }
        }

    }
}