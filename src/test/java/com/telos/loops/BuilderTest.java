package com.telos.loops;

import com.telos.loops.apikey.ApiKeyTestResponse;
import com.telos.loops.contacts.*;
import com.telos.loops.events.EventResponse;
import com.telos.loops.events.EventSendRequest;
import com.telos.loops.lists.MailingList;
import com.telos.loops.model.RequestOptions;
import com.telos.loops.properties.ContactProperty;
import com.telos.loops.properties.ContactPropertyCreateRequest;
import com.telos.loops.properties.ContactPropertyResponse;
import com.telos.loops.transactional.*;
import com.telos.loops.transport.HttpMethod;
import com.telos.loops.transport.TransportRequest;
import com.telos.loops.transport.TransportResponse;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderTest {

    @Test
    void testContactCreateRequestBuilder() {
        ContactCreateRequest request = ContactCreateRequest.builder()
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .subscribed(true)
                .userGroup("group")
                .userId("uid")
                .putMailingList("listId", true)
                .putAdditionalProperty("customProp", "value")
                .build();

        assertThat(request.email()).isEqualTo("test@example.com");
        assertThat(request.firstName()).isEqualTo("Test");
        assertThat(request.lastName()).isEqualTo("User");
        assertThat(request.subscribed()).isTrue();
        assertThat(request.userGroup()).isEqualTo("group");
        assertThat(request.userId()).isEqualTo("uid");
        assertThat(request.mailingLists()).containsEntry("listId", true);
        assertThat(request.additionalProperties()).containsEntry("customProp", "value");
    }

    @Test
    void testContactUpdateRequestBuilder() {
        ContactUpdateRequest request = ContactUpdateRequest.builder()
                .email("user@example.com")
                .firstName("NewName")
                .putMailingList("list2", false)
                .putAdditionalProperty("prop2", 123)
                .build();

        assertThat(request.email()).isEqualTo("user@example.com");
        assertThat(request.firstName()).isEqualTo("NewName");
        assertThat(request.lastName()).isNull();
        assertThat(request.mailingLists()).containsEntry("list2", false);
        assertThat(request.additionalProperties()).containsEntry("prop2", 123);
    }

    @Test
    void testContactDeleteRequestBuilder() {
        ContactDeleteRequest request = ContactDeleteRequest.builder()
                .email("del@example.com")
                .userId("delUid")
                .build();
        assertThat(request.email()).isEqualTo("del@example.com");
        assertThat(request.userId()).isEqualTo("delUid");
    }

    @Test
    void testContactFindRequestBuilder() {
        ContactFindRequest request = ContactFindRequest.builder()
                .email("find@example.com")
                .userId("findUid")
                .build();
        assertThat(request.email()).isEqualTo("find@example.com");
        assertThat(request.userId()).isEqualTo("findUid");
    }

    @Test
    void testContactResponseBuilder() {
        ContactResponse response = ContactResponse.builder()
                .id("cid")
                .message("created")
                .build();
        assertThat(response.id()).isEqualTo("cid");
        assertThat(response.message()).isEqualTo("created");
    }

    @Test
    void testContactBuilder() {
        Contact contact = Contact.builder()
                .id("id")
                .email("c@example.com")
                .firstName("F")
                .lastName("L")
                .source("api")
                .subscribed(true)
                .userGroup("ug")
                .userId("uid")
                .putMailingList("list", true)
                .optInStatus("confirmed")
                .build();
        assertThat(contact.email()).isEqualTo("c@example.com");
        assertThat(contact.mailingLists()).containsEntry("list", true);
    }

    @Test
    void testEventSendRequestBuilder() {
        EventSendRequest request = EventSendRequest.builder()
                .email("user@example.com")
                .userId("uid")
                .eventName("purchase")
                .putEventProperty("amount", 100)
                .putMailingList("list1", true)
                .putAdditionalProperty("prop", "val")
                .build();

        assertThat(request.email()).isEqualTo("user@example.com");
        assertThat(request.userId()).isEqualTo("uid");
        assertThat(request.eventName()).isEqualTo("purchase");
        assertThat(request.eventProperties()).containsEntry("amount", 100);
        assertThat(request.mailingLists()).containsEntry("list1", true);
        assertThat(request.additionalProperties()).containsEntry("prop", "val");
    }

    @Test
    void testEventResponseBuilder() {
        EventResponse response = EventResponse.builder().success(true).build();
        assertThat(response.success()).isTrue();
    }

    @Test
    void testTransactionalSendRequestBuilder() {
        TransactionalSendRequest.Attachment attachment = TransactionalSendRequest.Attachment.builder()
                .filename("test.pdf")
                .contentType("application/pdf")
                .data("base64data")
                .build();

        TransactionalSendRequest request = TransactionalSendRequest.builder()
                .transactionalId("transId")
                .email("user@example.com")
                .addToAudience(true)
                .putDataVariable("var", "val")
                .addAttachment(attachment)
                .build();

        assertThat(request.transactionalId()).isEqualTo("transId");
        assertThat(request.email()).isEqualTo("user@example.com");
        assertThat(request.addToAudience()).isTrue();
        assertThat(request.dataVariables()).containsEntry("var", "val");
        assertThat(request.attachments()).hasSize(1);
    }

    @Test
    void testTransactionalResponseBuilder() {
        TransactionalResponse response = TransactionalResponse.builder().success(false).build();
        assertThat(response.success()).isFalse();
    }

    @Test
    void testTransactionalEmailBuilder() {
        TransactionalEmail email = TransactionalEmail.builder()
                .id("tid")
                .name("tname")
                .lastUpdated("date")
                .addDataVariable("v1")
                .build();
        assertThat(email.id()).isEqualTo("tid");
        assertThat(email.dataVariables()).contains("v1");
    }

    @Test
    void testTransactionalListResponseBuilder() {
        TransactionalListResponse.Pagination pagination = TransactionalListResponse.Pagination.builder()
                .count(10)
                .nextCursor("next")
                .build();

        TransactionalListResponse response = TransactionalListResponse.builder()
                .addTransactionalEmail(new TransactionalEmail("id", "name", "date", List.of()))
                .pagination(pagination)
                .build();

        assertThat(response.transactionalEmails()).hasSize(1);
        assertThat(response.pagination().count()).isEqualTo(10);
    }

    @Test
    void testContactPropertyCreateRequestBuilder() {
        ContactPropertyCreateRequest request = ContactPropertyCreateRequest.builder()
                .name("propName")
                .type("string")
                .build();
        assertThat(request.name()).isEqualTo("propName");
        assertThat(request.type()).isEqualTo("string");
    }

    @Test
    void testContactPropertyResponseBuilder() {
        ContactPropertyResponse response = ContactPropertyResponse.builder().success(true).build();
        assertThat(response.success()).isTrue();
    }

    @Test
    void testContactPropertyBuilder() {
        ContactProperty property = ContactProperty.builder()
                .key("key")
                .label("Label")
                .type("string")
                .build();
        assertThat(property.key()).isEqualTo("key");
    }

    @Test
    void testMailingListBuilder() {
        MailingList list = MailingList.builder()
                .id("lid")
                .name("lname")
                .description("desc")
                .isPublic(true)
                .build();
        assertThat(list.id()).isEqualTo("lid");
        assertThat(list.isPublic()).isTrue();
    }

    @Test
    void testApiKeyTestResponseBuilder() {
        ApiKeyTestResponse response = ApiKeyTestResponse.builder()
                .success(true)
                .teamName("Team")
                .build();
        assertThat(response.success()).isTrue();
        assertThat(response.teamName()).isEqualTo("Team");
    }

    @Test
    void testRequestOptionsBuilder() {
        RequestOptions options = RequestOptions.builder()
                .addHeader("H-Key", "H-Val")
                .build();
        assertThat(options.headers()).containsEntry("H-Key", "H-Val");
    }

    @Test
    void testTransportRequestBuilder() {
        TransportRequest request = TransportRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url("http://example.com")
                .addHeader("Content-Type", "application/json")
                .body("body".getBytes(StandardCharsets.UTF_8))
                .build();
        assertThat(request.httpMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.headers()).containsEntry("Content-Type", "application/json");
        assertThat(new String(request.body(), StandardCharsets.UTF_8)).isEqualTo("body");
    }

    @Test
    void testTransportResponseBuilder() {
        TransportResponse response = TransportResponse.builder()
                .status(200)
                .addHeader("Server", "Jetty")
                .response("ok".getBytes(StandardCharsets.UTF_8))
                .build();
        assertThat(response.status()).isEqualTo(200);
        assertThat(response.headers()).containsEntry("Server", "Jetty");
        assertThat(new String(response.response(), StandardCharsets.UTF_8)).isEqualTo("ok");
    }
}
