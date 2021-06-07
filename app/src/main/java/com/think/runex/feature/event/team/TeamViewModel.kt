package com.think.runex.feature.event.team

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.base.BaseViewModel
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.*
import com.think.runex.feature.event.team.data.TeamImage
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TeamViewModel(private val repo: TeamRepository) : BaseViewModel() {

    var registerData: MutableLiveData<Registered> = MutableLiveData()

    var isTeamLeader: Boolean = false

    fun getRegisterData(body: RegisteredRequestBody) = launch(IO) {

        //Get my user info for check is team leader.
        if (TokenManager.userId.isBlank()) {
            val myUserInfoResult = repo.getMyUserInfo()
            if (myUserInfoResult.isSuccessful()) {
                TokenManager.updateUserId(myUserInfoResult.data?.id ?: "")
            }
        }

        val result = repo.getRegisterData(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        registerData.postValue(result.data)
    }

    suspend fun getUserInfoById(userId: String): UserInfo? = withContext(IO) {
        val result = repo.getUserInfoById(UserInfoRequestBody(userId))
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }
        return@withContext result.data
    }

    suspend fun getTeamImageUrl(): String? = withContext(IO) {

        val result = repo.getTeamImage(TeamImage(getLeaderRegistrationData()?.id ?: ""))

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.data?.iconUrl
    }

    suspend fun addMemberToTeam(userToAdd: UserInfo): Boolean = withContext(IO) {

        val eventDetail = registerData.value?.eventDetail ?: EventDetail()

        val teamLeaderRegisteredData = registerData.value?.getTeamLeaderRegisteredData()
            ?: RegisteredData()

        val teamLeaderTicketOption = teamLeaderRegisteredData.ticketOptions?.firstOrNull()
            ?: TicketOptionEventRegistration()

        val teamLeaderUserOption = teamLeaderTicketOption.userOption
            ?: UserOptionEventRegistration()

        val ticketAtRegister = teamLeaderTicketOption.ticket ?: Ticket()

        val gSon = Gson()

        val userOptionObject = JsonObject().apply {
            //addProperty("address", "")
            //addProperty("birthdate", userToAdd.birthDate ?: "")
            addProperty("blood_type", userToAdd.bloodType ?: "")
            addProperty("citycen_id", userToAdd.citizenId ?: "")
            addProperty("color", teamLeaderUserOption.color)
            //addProperty("confirm", userToAdd.isConfirmed)
            //addProperty("created_at", userToAdd.createdAt ?: "")
            //addProperty("emergency_contact", userToAdd.emergencyContact ?: "")
            //addProperty("emergency_phone", userToAdd.emergencyPhone ?: "")
            addProperty("firstname", userToAdd.firstName ?: "")
            //addProperty("firstname_th", userToAdd.firstNameTh ?: "")
            addProperty("gender", userToAdd.gender ?: "")
            //addProperty("home_no", "")
            addProperty("lastname", userToAdd.lastName ?: "")
            addProperty("fullname", userToAdd.getFullName())
            //addProperty("lastname_th", userToAdd.lastNameTh ?: "")
            //addProperty("moo", "")
            //addProperty("nationality", userToAdd.nationality ?: "")
            //addProperty("passport", userToAdd.passport ?: "")
            addProperty("phone", userToAdd.phone ?: "")
            //add("tambon", gSon.toJsonTree(SubDistrict()))
            addProperty("team", teamLeaderUserOption.team)
            addProperty("zone", teamLeaderUserOption.zone)
        }

        val ticketObject = JsonObject().apply {
            addProperty("reciept_type", teamLeaderTicketOption.receiptType)
            //addProperty("register_number", "")
            addProperty("total_price", 0)
            //add("shirts", gSon.toJsonTree(Shirt()))
            add("tickets", gSon.toJsonTree(ticketAtRegister))
            add("user_option", userOptionObject)
        }

        val ticketObjects = JsonArray().apply {
            add(ticketObject)
        }

        val registerObject = JsonObject().apply {
            add("coupon", gSon.toJsonTree(teamLeaderRegisteredData.coupon))
            //addProperty("created_at", "")
            addProperty("discount_price", 0)
            addProperty("event_code", teamLeaderRegisteredData.eventCode ?: "")
            addProperty("event_id", teamLeaderRegisteredData.eventId ?: "")
            //addProperty("id","")
            addProperty("is_team_lead", false)
            addProperty("order_id", teamLeaderRegisteredData.orderId ?: "")
            addProperty("parent_reg_id", teamLeaderRegisteredData.parentRegisterId ?: "")
            add("partner", gSon.toJsonTree(teamLeaderRegisteredData.partner))
            addProperty("payment_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
            addProperty("payment_type", RegisterStatus.SUCCESS)
            addProperty("reg_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
            addProperty("status", teamLeaderRegisteredData.status ?: "")
            addProperty("ticket_id", ticketAtRegister.id ?: "")
            add("ticket_options", ticketObjects)
            addProperty("total_price", 0)
            //addProperty("updated_at", "")
            addProperty("user_id", userToAdd.id ?: "")
        }

        val body = JsonObject().apply {
            add("event", gSon.toJsonTree(eventDetail))
            addProperty("event_code", eventDetail.code ?: "")
            addProperty("event_id", eventDetail.id ?: 0)
            addProperty("parent_reg_id", teamLeaderRegisteredData.parentRegisterId)
            add("regs", registerObject)
            addProperty("team_user_id", userToAdd.id ?: "")
        }

        val result = repo.addMEmberToTeam(body)

        when (result.isSuccessful()) {
            true -> withContext(IO) {

                //Update register data
                val registerDataBody = RegisteredRequestBody().apply {
                    eventCode = eventDetail.code ?: ""
                    registerId = teamLeaderRegisteredData.id ?: ""
                    parentRegisterId = teamLeaderRegisteredData.parentRegisterId ?: ""
                    ticketId = teamLeaderTicketOption.ticket?.id ?: ""
                }

                val updateRegisterDataResult = repo.getRegisterData(registerDataBody)

                if (updateRegisterDataResult.isSuccessful()) {
                    registerData.postValue(updateRegisterDataResult.data)
                }
            }
            false -> onHandleError(result.code, result.message)
        }

        return@withContext result.isSuccessful()
    }

    fun getMyRegistrationData(): RegisteredData? {
        return registerData.value?.registeredDataList?.find { it.userId == TokenManager.userId }
    }

    fun getLeaderRegistrationData(): RegisteredData? {
        return registerData.value?.registeredDataList?.find { it.isTeamLead == true }
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TeamViewModel(TeamRepository(ApiService().provideService(context, TeamApi::class.java))) as T
        }
    }
}