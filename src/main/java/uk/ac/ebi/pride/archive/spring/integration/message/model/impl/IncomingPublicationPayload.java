package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;
import uk.ac.ebi.pride.archive.spring.integration.message.model.ProjectAccessionPayload;
import uk.ac.ebi.pride.archive.spring.integration.message.model.PublicationPayload;

import java.io.Serializable;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class IncomingPublicationPayload implements ProjectAccessionPayload, PublicationPayload, Serializable {

    @NonNull
    private String accession;
    @NonNull
    private String pubMedId;
    @NonNull
    private Date publicationDate;
}
