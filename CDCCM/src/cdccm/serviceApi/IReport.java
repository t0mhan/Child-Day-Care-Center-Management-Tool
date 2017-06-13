package cdccm.serviceApi;

import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public interface IReport {
	public void createReport(int childid);

//	public JasperPrint getReport() throws ColumnBuilderException, JRException, ClassNotFoundException;
//
//	Style createHeaderStyle();
//
//	Style createDetailNumberStyle();
//
//	Style createDetailTextStyle();
//
//	DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle) throws ColumnBuilderException, ClassNotFoundException;
//
//	AbstractColumn createColumn(String property, Class type, String title, int width, Style headerStyle,
//			Style detailStyle);
}
