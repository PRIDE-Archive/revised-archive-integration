package uk.ac.ebi.pride.archive.spring.integration.message.model;


import uk.ac.ebi.pride.archive.dataprovider.project.SubmissionType;

import java.util.Date;

/**
 * General interface representing project related messaging payload
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface SubmissionPayload {

    SubmissionType getSubmissionType();
    Date getSubmissionDate();
}
