package com.think.runex.feature.event.team

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.*
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TeamViewModel(private val repo: TeamRepository) : BaseViewModel() {

    var registerData: MutableLiveData<Registered> = MutableLiveData()

    fun getRegisterData(eventCode: String, registerId: String, parentRegisterId: String) = launch(IO) {

        val result = repo.getRegisterData(eventCode, registerId, parentRegisterId)

        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        registerData.postValue(result.data)
    }

    suspend fun getUserInfoById(userId: String): UserInfo? = withContext(IO) {
        val result = repo.getUserInfoById(userId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    suspend fun addMemberToTeam(userToAdd: UserInfo): Boolean = withContext(IO) {

        val parentRegisterData = registerData.value?.getParentRegisterData() ?: RegisteredData()
        val eventDetail = registerData.value?.eventDetail ?: EventDetail()
        val parentTicketOption = parentRegisterData.ticketOptions?.firstOrNull()
                ?: TicketOptionEventRegistration()
        val parentUserOption = parentTicketOption.userOption ?: UserOptionEventRegistration()
        val ticket = parentTicketOption.ticket ?: Ticket()
        val gSon = Gson()

        val userOptionObject = JsonObject().apply {
            addProperty("address", "")
            addProperty("birthdate", userToAdd.birthDate ?: "")
            addProperty("blood_type", userToAdd.bloodType ?: "")
            addProperty("citycen_id", userToAdd.citizenId ?: "")
            addProperty("color", parentUserOption.color)
            addProperty("confirm", userToAdd.isConfirmed)
            addProperty("created_at", userToAdd.createdAt ?: "")
            addProperty("emergency_contact", userToAdd.emergencyContact ?: "")
            addProperty("emergency_phone", userToAdd.emergencyPhone ?: "")
            addProperty("firstname", userToAdd.firstName ?: "")
            addProperty("firstname_th", userToAdd.firstNameTh ?: "")
            addProperty("gender", userToAdd.gender ?: "")
            addProperty("home_no", "")
            addProperty("lastname", userToAdd.lastName ?: "")
            addProperty("lastname_th", userToAdd.lastNameTh ?: "")
            addProperty("moo", "")
            addProperty("nationality", userToAdd.nationality ?: "")
            addProperty("passport", userToAdd.passport ?: "")
            addProperty("phone", userToAdd.phone ?: "")
            add("tambon", gSon.toJsonTree(SubDistrict()))
            addProperty("team", parentUserOption.team)
            addProperty("zone", parentUserOption.zone)
        }

        val ticketObject = JsonObject().apply {
            addProperty("reciept_type", parentTicketOption.receiptType)
            //addProperty("register_number", "")
            addProperty("total_price", parentTicketOption.totalPrice)
            add("shirts", gSon.toJsonTree(Shirt()))
            add("tickets", gSon.toJsonTree(ticket))
            add("user_option", userOptionObject)
        }

        val ticketObjects = JsonArray().apply {
            add(ticketObject)
        }

        val registerObject = JsonObject().apply {
            add("coupon", gSon.toJsonTree(parentRegisterData.coupon))
            //addProperty("created_at", "")
            addProperty("discount_price", parentRegisterData.discountPrice ?: 0.0)
            addProperty("event_code", parentRegisterData.eventCode ?: "")
            addProperty("event_id", parentRegisterData.eventId ?: "")
            //addProperty("id","")
            addProperty("is_team_lead", false)
            addProperty("order_id", parentRegisterData.orderId ?: "")
            addProperty("parent_reg_id", parentRegisterData.parentRegisterId ?: "")
            add("partner", gSon.toJsonTree(parentRegisterData.partner))
            addProperty("payment_date", parentRegisterData.paymentDate ?: "")
            addProperty("payment_type", parentRegisterData.paymentType ?: "")
            addProperty("reg_date", parentRegisterData.registerDate ?: "")
            addProperty("status", parentRegisterData.status ?: "")
            addProperty("ticket_id", ticket.id ?: "")
            add("ticket_options", ticketObjects)
            addProperty("total_price", parentRegisterData.totalPrice ?: 0.0)
            //addProperty("updated_at", "")
            addProperty("user_id", userToAdd.id ?: "")
        }

        val body = JsonObject().apply {
            add("event", gSon.toJsonTree(eventDetail))
            addProperty("event_code", eventDetail.code ?: "")
            addProperty("event_id", eventDetail.id ?: 0)
            addProperty("parent_reg_id", parentRegisterData.parentRegisterId)
            add("regs", registerObject)
            addProperty("team_user_id", userToAdd.id ?: "")
        }

        val result = repo.addMEmberToTeam(body)

        when (result.isSuccessful()) {
            true -> withContext(IO) {

                //Update register data
                val updateRegisterDataResult = repo.getRegisterData(
                        eventCode = eventDetail.code ?: "",
                        registerId = parentRegisterData.id ?: "",
                        parentRegisterId = parentRegisterData.parentRegisterId ?: "")

                if (updateRegisterDataResult.isSuccessful()) {
                    registerData.postValue(updateRegisterDataResult.data)
                }
            }
            false -> onHandleError(result.statusCode, result.message)
        }

        return@withContext result.isSuccessful()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TeamViewModel(TeamRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}