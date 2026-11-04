const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read user profile", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create and read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const profileRef = aliceDb.collection("users").doc(ALICE_UID);
  await assertSucceeds(
    profileRef.set({
      userId: ALICE_UID,
      displayName: "Alice Montana",
      createdAt: new Date(),
    })
  );
  await assertSucceeds(profileRef.get());
});

test("Authenticated user: cannot read another user's profile", async () => {
  // Seed Alice's profile
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice Montana",
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create and read their own sessions and tools", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const sessionRef = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("sessions")
    .doc("sess_1");

  await assertSucceeds(
    sessionRef.set({
      id: "sess_1",
      userId: ALICE_UID,
      title: "Operación Alfa",
      lastMessagePreview: "Listo",
      messageCount: 1,
      createdAt: new Date(),
    })
  );

  const toolRef = aliceDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("tools")
    .doc("tool_1");

  await assertSucceeds(
    toolRef.set({
      id: "tool_1",
      userId: ALICE_UID,
      name: "Radar",
      purpose: "Vision",
      category: "Visión",
      isEnabled: true,
      executionCount: 0,
      createdAt: new Date(),
    })
  );
});

test("Cross-user: Bob cannot write to Alice's sessions", async () => {
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const sessionRef = bobDb
    .collection("users")
    .doc(ALICE_UID)
    .collection("sessions")
    .doc("sess_1");

  await assertFails(
    sessionRef.set({
      id: "sess_1",
      userId: BOB_UID,
      title: "Hack",
      lastMessagePreview: "X",
      messageCount: 0,
      createdAt: new Date(),
    })
  );
});
