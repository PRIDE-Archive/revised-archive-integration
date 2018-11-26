package uk.ac.ebi.pride.archive.spring.integration.message.model.impl;

import lombok.*;
import uk.ac.ebi.pride.archive.dataprovider.project.SubmissionType;
import uk.ac.ebi.pride.archive.spring.integration.message.model.ProjectAccessionPayload;
import uk.ac.ebi.pride.archive.spring.integration.message.model.SubmissionPayload;

import java.io.Serializable;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Data
public class IncomingSubmissionPayload implements ProjectAccessionPayload, SubmissionPayload, Serializable {
    @NonNull
    private String accession;
    @NonNull
    private SubmissionType submissionType;
    @NonNull
    private Date submissionDate;
}
