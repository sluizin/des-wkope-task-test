package des.wangku.operate.task.test;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import des.wangku.operate.standard.database.MainSource;
import des.wangku.operate.standard.swt.DBACCESSCTabFolder;
import des.wangku.operate.standard.swt.DBMYSQLCTabFolder;
import des.wangku.operate.standard.swt.ExcelCTabFolder;
import des.wangku.operate.standard.task.AbstractTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * UI测试平台
 * @author Sunjian
 * @version 1.0
 * @since jdk1.8
 */
public class WKOPETaskTest extends AbstractTask {
	/** 日志 */
	static Logger logger = Logger.getLogger(WKOPETaskTest.class);
	final Display display = Display.getDefault();
	WKOPETaskTest base = this;
	ExcelCTabFolder excelCTabFolder = null;
	DBMYSQLCTabFolder dbmysqlCTabFolder = null;
	DBACCESSCTabFolder dbaccessCTabFolder = null;
	ExpandBar expandBar = null;
	private Text sqlinput;
	private Text txtNew;

	public WKOPETaskTest(Composite parent) {
		super(parent);
	}

	public WKOPETaskTest(Composite parent, int style) {
		super(parent, style, WKOPETaskTest.class, AbstractTask.MVver | AbstractTask.MVmodQuit | AbstractTask.MVsysQuit);
		excelCTabFolder = new ExcelCTabFolder(this, SWT.NONE);
		excelCTabFolder.setBounds(10, 10, 868, 108);
		dbmysqlCTabFolder = new DBMYSQLCTabFolder(this, SWT.None, "0", this.getProProperties(), MainSource.getConnWKjixiao());
		dbmysqlCTabFolder.setBounds(10, 358, 868, 56);


		dbaccessCTabFolder = new DBACCESSCTabFolder(this, SWT.None, "0",base);
		dbaccessCTabFolder.setBounds(22, 130, 868, 115);
		
		
		Group grpSql = new Group(this, SWT.NONE);
		grpSql.setText("SQL");
		grpSql.setBounds(10, 420, 868, 130);

		sqlinput = new Text(grpSql, SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		String sql="select id as '序号',depart_name as '部门',email,is_delete as '删除',leader_id as '上级id',mobile_phone as '手机',`name` as '姓名'," + 
				"`no` as '公司编号',organization_name_two as '二级部门',organization_name as '部门',post as '职位',user_type as '人员类型'," + 
				"leader_nos as '领导等级' from user limit 10";
		sqlinput.setText(sql);
		sqlinput.setBounds(10, 20, 328, 100);
		/*GridData gd_cmdIntro = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		        1);
		gd_cmdIntro.widthHint = 100; // 必须得设置宽度，否则自动换行不好使
		gd_cmdIntro.heightHint = 200;
		sqlinput.setLayoutData(gd_cmdIntro);*/
		
		
		
		Button btnNewButton = new Button(grpSql, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String sql = sqlinput.getText();
				String tablename=txtNew.getText();
				String jsonStr=sqlparainput.getText();
				dbmysqlCTabFolder.addDBResultTable(tablename, sql,jsonStr);
				dbmysqlCTabFolder.putPopMenu();
			}
		});
		btnNewButton.setBounds(376, 86, 68, 23);
		btnNewButton.setText("生成表");
		
		txtNew = new Text(grpSql, SWT.BORDER);
		txtNew.setText("new");
		txtNew.setBounds(357, 20, 102, 19);
		
		sqlparainput = new Text(grpSql, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		sqlparainput.setBounds(677, 17, 181, 103);
		sqlparainput.setText(jsonStr);
	}
	static String jsonStr="{\"title\":\"test\",\"isViewHead\":true,\"headRowSuffix\":0,"
			+ "\"widthArray\":[50, 110, 150,70,60,90,55,70,90,90,100,90],\"defTCWidth\":150,"
			+ "\"isAutoWidth\":false,\"isAutoremoveUpNullRows\":false,"
			+ "\"isAutoremoveDownNullRows\":true,\"isTrim\":false,"
			+ "\"readonlySuffix\":[0],attrSuffix:true}";
	private Text sqlparainput;
	@Override
	public void changeAfter(Object obj) {

	}

	@Override
	public void collect() {

	}

	@Override
	public String getMenuName() {
		return "UI测试平台";
	}

	@Override
	public String getMenuNameHead() {
		return "P000";
	}

	@Override
	public void mainWorkThreadBreak() {

	}

	@Override
	public boolean getMainWorkThreadState() {
		return false;
	}

	@Override
	public String getVersionFileJarFullPath() {
		return "/update.info";
	}

	@Override
	public void initCompositeMenu() {

	}

	@Override
	public void multiThreadOnRun() {

	}

	@Override
	public void multiThreadOnRunEnd() {

	}
}
