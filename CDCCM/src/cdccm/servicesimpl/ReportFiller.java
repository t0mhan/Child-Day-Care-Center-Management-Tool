package cdccm.servicesimpl;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.ActivityPOJO;
import cdccm.pojo.ChildNamePlate;
import cdccm.pojo.ChildReportPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.SchedulePOJO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportFiller {
	private Collection<ChildReportPOJO> reportlist = new ArrayList<>();
	private final Collection<SchedulePOJO> schedulelist = new ArrayList<>();
	private MySQLDBConnector dbConnector;
	AdminServiceImpl adminservice = null;

	public ReportFiller(Collection<? extends Object> listOfObjectsforReport) {
		dbConnector = MySQLDBConnector.getInstance();
		for (Object obj : listOfObjectsforReport) {
			if (obj.getClass().getName().equals("cdccm.pojo.ChildReportPOJO")) {
				System.out.println("Inside cdccm.pojo.ChildReportPOJO");
				this.reportlist = (Collection<ChildReportPOJO>) listOfObjectsforReport;
				// reportlist.addAll((Collection<? extends
				// ChildReportPOJO>)option);
				break;
			} else if (obj.getClass().getName().equals("cdccm.pojo.SchedulePOJO")) {
				schedulelist.addAll((Collection<? extends SchedulePOJO>) listOfObjectsforReport);
				adminservice = new AdminServiceImpl();
				break;
			}
		}
	}

	public JasperPrint getReport(String typeofreport, int childid)
			throws ColumnBuilderException, JRException, ClassNotFoundException, SQLException {

		List<FoodPOJO> listOfFood = new ArrayList<>();
		List<ActivityPOJO> activityDescList = new ArrayList<>();

		JasperPrint jp = null;
		JasperPrint jp1 = null;
		JasperPrint jp2 = null;
		Style headerStyle = createHeaderStyle();// style setup
		Style detailTextStyle = createDetailTextStyle();
		Style detailNumberStyle = createDetailNumberStyle();
		/* check whether report request is for schedule or performance */
		if (typeofreport.equals("performancereport")) {
			/* make call for performance report skeleton creation */
			DynamicReport dynaPerformanceReport = getPerformanceReport(headerStyle, detailTextStyle, detailNumberStyle);
			/* fill the empty skeleton */
			jp = DynamicJasperHelper.generateJasperPrint(dynaPerformanceReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(reportlist));
		} else if (typeofreport.equals("schedulereport")) {

			System.out.println("cdccm.pojo.SchedulePOJO");
			/* make call for getScheduleReport report skeleton creation */
			DynamicReport dynaScheduleReport = getScheduleReport(headerStyle, detailTextStyle, detailNumberStyle,
					childid);
			/* fill the empty skeleton */
			jp = DynamicJasperHelper.generateJasperPrint(dynaScheduleReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(schedulelist));

			/* access adminservice to get ActivityDescription */
			activityDescList = this.adminservice.getActivityDescription(childid);
			/* make call to getActivityDescReport report skeleton creation */
			DynamicReport dynaActivityDescReport = getActivityDescReport(headerStyle, detailTextStyle,
					detailNumberStyle, childid);
			/* fill the empty skeleton */
			jp1 = DynamicJasperHelper.generateJasperPrint(dynaActivityDescReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(activityDescList));

			/* access adminservice to get extractfood() */
			listOfFood = this.adminservice.extractfood();
			/* make call for getFoodReport report skeleton creation */
			DynamicReport dynaFoodReport = getFoodReport(headerStyle, detailTextStyle, detailNumberStyle, childid);
			/* fill the empty skeleton */
			jp2 = DynamicJasperHelper.generateJasperPrint(dynaFoodReport, new ClassicLayoutManager(),
					new JRBeanCollectionDataSource(listOfFood));

			List foodpages = jp2.getPages();
			for (int j = 0; j < foodpages.size(); j++) {
				JRPrintPage object = (JRPrintPage) foodpages.get(j);
				jp1.addPage(object);
			}
			List activityAndFoodPages = jp1.getPages();
			for (int j = 0; j < activityAndFoodPages.size(); j++) {
				JRPrintPage object = (JRPrintPage) activityAndFoodPages.get(j);
				jp.addPage(object);
			}
		}
		return jp;
	}

	/* create report for food */
	private DynamicReport getActivityDescReport(Style headerStyle, Style detailTextStyle, Style detailNumberStyle,
			int childid) {
		DynamicReport foodreport = null;
		FastReportBuilder rb = new FastReportBuilder();

		AbstractColumn activityName = createColumn("name", String.class, "Activity Name", 40, headerStyle,
				detailTextStyle);// creates
		AbstractColumn activityDescription = createColumn("description", String.class, "Activity Description", 100,
				headerStyle, detailTextStyle);

		rb.addColumn(activityName).addColumn(activityDescription);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		rb.setTitle("Activity Related Information:(" + date + ")");
		rb.setTitleStyle(titleStyle.build()).setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		;

		rb.setUseFullPageWidth(true);
		System.out.println("getActivityDescReport " + childid);
		foodreport = rb.build();
		return foodreport;

	}

	/* create report for Activity and Description */
	private DynamicReport getFoodReport(Style headerStyle, Style detailTextStyle, Style detailNumberStyle,
			int childid) {
		DynamicReport foodreport = null;
		FastReportBuilder rb = new FastReportBuilder();
		AbstractColumn day = createColumn("day", String.class, "Day of the Week", 50, headerStyle, detailTextStyle);
		AbstractColumn breakfast = createColumn("breakfast", String.class, "BREAKFAST", 50, headerStyle,
				detailTextStyle);// creates
		AbstractColumn lunch = createColumn("lunch", String.class, "LUNCH", 50, headerStyle, detailTextStyle);
		AbstractColumn snacks = createColumn("snack", String.class, "SNACKS", 50, headerStyle, detailTextStyle);

		rb.addColumn(day).addColumn(breakfast).addColumn(lunch).addColumn(snacks);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());

		rb.setTitle("CHILD FOOD PLAN OF THE WEEK,  Date:(" + date + ")");
		rb.setTitleStyle(titleStyle.build());
		rb.setTitleStyle(titleStyle.build()).setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		rb.setUseFullPageWidth(true);

		foodreport = rb.build();
		;
		return foodreport;
	}

	private DynamicReport getScheduleReport(Style headerStyle, Style detailTextStyle, Style detailNumberStyle,
			int childid) throws ColumnBuilderException, ClassNotFoundException {
		ChildNamePlate childnameplate = null;
		DynamicReportBuilder report = new DynamicReportBuilder();

		childnameplate = getchildNameplate(childid);// takes child nameplate

		AbstractColumn plantime = createColumn("plantime", String.class, "Time", 50, headerStyle, detailTextStyle);// creates
																													// column
		AbstractColumn plan = createColumn("plan", String.class, "Activity", 50, headerStyle, detailTextStyle);
		report.addColumn(plantime).addColumn(plan);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		report.setTitle("Child Plan of the week,  Date:(" + date + ")");
		report.setTitleStyle(titleStyle.build());

		report.setSubtitle("Roll No: " + childid + ". Name: " + childnameplate.getChild_first_name() + ". Surname: "
				+ childnameplate.getChild_last_name() + ". DOB: " + childnameplate.getDate_of_birth() + ". Group: "
				+ childnameplate.getAge_group())
				.setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleHeight(40);
		report.setSubtitleStyle(subTitleStyle1.build());
		report.setUseFullPageWidth(true);
		System.out.println("insede printReport 2 " + childid);

		return report.build();

	}

	// a service which communicates with db and takes child name plate
	private ChildNamePlate getchildNameplate(int childid) {

		ChildNamePlate childnameplate = null;

		final String sql = "select ci.name,ci.surname,ci.dob,ag.name " + "from child_info ci join age_group ag "
				+ "on(ci.fk_age_group=ag.idage_group) " + "where ci.idchild=?;";
		try {
			ResultSet resultset = dbConnector.getReport(sql, childid);
			if (resultset.next()) {
				childnameplate = new ChildNamePlate(resultset.getString(1), resultset.getString(2),
						resultset.getString(3), resultset.getString(4));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return childnameplate;

	}

	// follwoing method return the style object for: headers,content,numbers
	// ezc.
	private Style createHeaderStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.COMIC_SANS_BIG_BOLD);
		sb.setBorder(Border.THIN());

		sb.setBorder(Border.PEN_2_POINT());
		sb.setHorizontalAlign(HorizontalAlign.CENTER);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setTextColor(Color.BLACK);
		sb.setBackgroundColor(Color.PINK);
		sb.setTransparency(Transparency.OPAQUE);
		return sb.build();
	}

	private Style createDetailTextStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM);
		sb.setBorder(Border.THIN());
		sb.setTextColor(Color.GREEN);
		sb.setHorizontalAlign(HorizontalAlign.LEFT);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setPaddingLeft(5);
		return sb.build();
	}

	private Style createDetailNumberStyle() {
		StyleBuilder sb = new StyleBuilder(true);
		sb.setFont(Font.VERDANA_MEDIUM);
		sb.setBorder(Border.DOTTED());
		sb.setHorizontalAlign(HorizontalAlign.RIGHT);
		sb.setVerticalAlign(VerticalAlign.MIDDLE);
		sb.setPaddingRight(5);
		return sb.build();
	}

	private AbstractColumn createColumn(String property, Class type, String title, int width, Style headerStyle,
			Style detailStyle) throws ColumnBuilderException {
		AbstractColumn columnState = ColumnBuilder.getNew().setColumnProperty(property, type.getName()).setTitle(title)
				.setWidth(Integer.valueOf(width)).setStyle(detailStyle).setHeaderStyle(headerStyle).build();
		return columnState;
	}

	private DynamicReport getPerformanceReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle)
			throws ColumnBuilderException, ClassNotFoundException {

		DynamicReportBuilder report = new DynamicReportBuilder();

		AbstractColumn activityName = createColumn("activityName", String.class, "Activity Name", 50, headerStyle,
				detailTextStyle);
		AbstractColumn sessionname = createColumn("sessionname", String.class, "Session", 50, headerStyle,
				detailTextStyle);
		AbstractColumn providername = createColumn("providername", String.class, "Provider Name", 50, headerStyle,
				detailTextStyle);
		AbstractColumn feedback = createColumn("feedback", String.class, "feedback", 100, headerStyle, detailTextStyle);

		report.addColumn(activityName).addColumn(sessionname).addColumn(providername).addColumn(feedback);

		StyleBuilder titleStyle = new StyleBuilder(true);
		titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
		titleStyle.setBackgroundColor(Color.YELLOW);
		titleStyle.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle1 = new StyleBuilder(true);
		subTitleStyle1.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle1.setBackgroundColor(Color.PINK);
		subTitleStyle1.setFont(Font.COMIC_SANS_BIG);

		StyleBuilder subTitleStyle2 = new StyleBuilder(true);
		subTitleStyle2.setHorizontalAlign(HorizontalAlign.JUSTIFY);
		subTitleStyle2.setBackgroundColor(Color.PINK);
		subTitleStyle2.setFont(Font.COMIC_SANS_BIG);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(new Date());
		report.setTitle("Child Report   (on:" + date + ")");
		report.setTitleStyle(titleStyle.build());

		report.setSubtitle(date).setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleStyle(subTitleStyle2.build());

		ChildReportPOJO childreportpojo = reportlist.iterator().next();

		report.setSubtitle("RollNO: " + childreportpojo.getChildid() + ". Name: " + childreportpojo.getName()
				+ ". Surname: " + childreportpojo.getSurname() + ". Group: " + childreportpojo.getAgeGroup() + ". DOB: "
				+ childreportpojo.getDateOfBirth())
				.setDefaultStyles(headerStyle, headerStyle, headerStyle, detailTextStyle);
		report.setSubtitleHeight(40);
		report.setSubtitleStyle(subTitleStyle1.build());

		report.setUseFullPageWidth(true);

		return report.build();
	}

}