package com.telos.loops.transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Represents a transactional email template in Loops.
 *
 * <p>Transactional emails are one-off emails sent in response to user actions (e.g., password
 * reset, order confirmation). Each template is identified by an ID and can accept dynamic data
 * variables.
 *
 * @param id the unique identifier for this transactional email template
 * @param name the display name of the transactional email
 * @param lastUpdated the timestamp when this template was last modified
 * @param dataVariables list of variable names that can be used in this template (e.g.,
 *     "resetLink", "orderNumber")
 */
@JsonIgnoreProperties(ignoreUnknown = false)
public record TransactionalEmail(
    String id, String name, String lastUpdated, List<String> dataVariables) {}
