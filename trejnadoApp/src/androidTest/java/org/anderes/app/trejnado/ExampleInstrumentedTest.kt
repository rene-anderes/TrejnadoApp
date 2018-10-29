package org.anderes.app.trejnado

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.google.firebase.database.*


import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val TRAINING_PROGRAM_CHILD = "trainings"

    @Test
    fun shouldSaveDataToFirebase() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
//        initializeApp(appContext)

        val mFirebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference();

        val settings1 = hashMapOf("Griff" to "5", "Polster" to "1")
        val machine1 = TrainingMachine("E1", 0, settings1)
        val settings2 = hashMapOf("Fuss" to "3", "Polster" to "4")
        val machine2 = TrainingMachine("F2.2", 1, settings2)
        val machines = listOf(machine1, machine2)
        val trainingProgram = TrainingProgram("Test", 1234567890, machines)
        var newKey: String? = null


        val postListener = object: ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Test", "------------- onChildChanged: " + dataSnapshot.key);
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                Log.d("Test", "------------- onChildAdded: " + dataSnapshot.key);
                Log.d("Test", "------------- value: " + dataSnapshot.value)
                Log.d("Test", "------------- previousChildName: " + previousChildName);

                val session = TrainingSession()
                session.trainingDate = 47329174971
                val unit1 = TrainingUnit()
                unit1.duration = 100
                unit1.machine = machine1
                unit1.weight = 72
                val units = listOf(unit1)
                session.units = units;
                trainingProgram.sessions = trainingProgram.sessions.plus(session)
                if (newKey != null && newKey.equals(dataSnapshot.key)) {

                    mFirebaseDatabaseReference.child(TRAINING_PROGRAM_CHILD)
                        .child(dataSnapshot.key!!)
                        .setValue(trainingProgram)
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        mFirebaseDatabaseReference.child(TRAINING_PROGRAM_CHILD).addChildEventListener(postListener)

        newKey = mFirebaseDatabaseReference.child(TRAINING_PROGRAM_CHILD).push().key
        trainingProgram.key = newKey
        mFirebaseDatabaseReference.child(TRAINING_PROGRAM_CHILD).child(newKey!!).setValue(trainingProgram)


        Thread.sleep(1000)

        Log.i("Test", "------------- FINISHED")

    }

}
