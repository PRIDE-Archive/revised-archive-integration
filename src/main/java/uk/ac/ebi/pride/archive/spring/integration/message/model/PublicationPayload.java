package uk.ac.ebi.pride.archive.spring.integration.message.model;

import java.util.Date;

/**
 * @author Rui Wang
 * @version $Id$
 */
public interface PublicationPayload {

    String getPubMedId();

    Date getPublicationDate();
}
