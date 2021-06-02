package com.study.bugit.utils;

import com.study.bugit.model.IssueModel;
import com.study.bugit.repository.IssueRepository;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IssueIdentificatorGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        Connection connection = session.connection();

        String projectName = ((IssueModel) object).getProject().getName();
        Long projectId = ((IssueModel) object).getProject().getId();

        String issueNumber = "";
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("" +
                    "SELECT issue_number " +
                    "FROM bugit.issues" +
                    " WHERE project_id = " + projectId + " " +
                    "and created_at = (SELECT MAX(created_at) FROM bugit.issues WHERE project_id=" + projectId + ")");
            rs.next();
            String lastIssueNumber = rs.getString("issue_number");
            issueNumber = projectName + "-" +
                    (Integer.parseInt(lastIssueNumber.substring(
                            lastIssueNumber.lastIndexOf("-") + 1
                    )) + 1);
        } catch (SQLException e) {
            issueNumber = projectName + "-1";
        }

        System.out.println(((IssueModel) object).getProject().getName());
        return issueNumber;
    }
}
