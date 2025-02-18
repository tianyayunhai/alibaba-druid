/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.alibaba.druid.sql.dialect.spark.ast;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.spark.visitor.SparkASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peiheng.qph
 * @version $Id: AntsparkCreateTableStatement.java, v 0.1 2018年09月14日 15:02 peiheng.qph Exp $
 */
public class SparkCreateTableStatement extends SQLCreateTableStatement {
    protected List<SQLAssignItem> mappedBy = new ArrayList<SQLAssignItem>(1);
    protected List<SQLExpr> skewedBy = new ArrayList<SQLExpr>();
    protected List<SQLExpr> skewedByOn = new ArrayList<SQLExpr>();
    protected Map<String, SQLObject> serdeProperties = new LinkedHashMap<String, SQLObject>();
    protected SQLExprTableSource datasource;

    public SparkCreateTableStatement() {
        super(DbType.spark);
    }

    public List<SQLAssignItem> getMappedBy() {
        return mappedBy;
    }

    public List<SQLExpr> getSkewedBy() {
        return skewedBy;
    }

    public void addSkewedBy(SQLExpr item) {
        item.setParent(this);
        this.skewedBy.add(item);
    }

    public List<SQLExpr> getSkewedByOn() {
        return skewedByOn;
    }

    public void addSkewedByOn(SQLExpr item) {
        item.setParent(this);
        this.skewedByOn.add(item);
    }

    public Map<String, SQLObject> getSerdeProperties() {
        return serdeProperties;
    }

    public void setDatasource(SQLExpr datasource) {
        this.datasource = new SQLExprTableSource(datasource);
    }

    /**
     * Getter method for property datasource.
     *
     * @return property value of datasource
     */
    public SQLExprTableSource getDatasource() {
        return datasource;
    }

    protected void accept0(SQLASTVisitor v) {
        if (v instanceof SparkASTVisitor) {
            accept0((SparkASTVisitor) v);
            return;
        }
        super.accept0(v);
    }

    protected void accept0(SparkASTVisitor v) {
        if (v.visit(this)) {
            acceptChild(v);
        }
        v.endVisit(this);
    }

    protected void acceptChild(SQLASTVisitor v) {
        super.acceptChild(v);

        acceptChild(v, datasource);
        acceptChild(v, skewedBy);
        acceptChild(v, skewedByOn);
        for (SQLObject item : serdeProperties.values()) {
            acceptChild(v, item);
        }
    }
}
