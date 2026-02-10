package com.telos.loops.lists;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.telos.loops.AsyncTestUtils;
import com.telos.loops.LoopsClient;
import com.telos.loops.TestFixtures;
import com.telos.loops.WireMockSetup;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MailingListsClientTest {

  private WireMockSetup wireMock;
  private LoopsClient client;

  @BeforeEach
  void setUp() {
    wireMock = new WireMockSetup();
    client = wireMock.createClient(TestFixtures.TEST_API_KEY);
  }

  @AfterEach
  void tearDown() {
    wireMock.stop();
  }

  @Test
  void shouldListMailingListsSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/lists", TestFixtures.mailingListsResponse());

    // When
    List<MailingList> lists = client.mailingLists().list();

    // Then
    assertThat(lists).isNotEmpty();
    assertThat(lists).hasSize(2);
    assertThat(lists.get(0).id()).isEqualTo("list-1");
    assertThat(lists.get(0).name()).isEqualTo("Newsletter");
    assertThat(lists.get(0).isPublic()).isTrue();
  }

  @Test
  void shouldListMailingListsAsyncSuccessfully() {
    // Given
    wireMock.stubGetSuccess("/lists", TestFixtures.mailingListsResponse());

    // When
    CompletableFuture<List<MailingList>> future = client.mailingLists().listAsync();

    // Then
    List<MailingList> lists = AsyncTestUtils.awaitCompletion(future);
    assertThat(lists).isNotEmpty();
    assertThat(lists).hasSize(2);
  }

  @Test
  void shouldReturnEmptyListWhenNoMailingListsExist() {
    // Given
    wireMock.stubGetSuccess("/lists", "[]");

    // When
    List<MailingList> lists = client.mailingLists().list();

    // Then
    assertThat(lists).isEmpty();
  }
}
